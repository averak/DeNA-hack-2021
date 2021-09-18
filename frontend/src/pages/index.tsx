import { Menu, Transition } from "@headlessui/react";
import { XIcon } from "@heroicons/react/solid";
import type { VFC } from "react";
import type { FormEvent } from "react";
import { useCallback, useState } from "react";
import AREA from "src/utils/static/area.json";

import { PlanContent } from "../organisms/PlanContent"

// type Area = {
//   [key: string]: string;
// };

// type AreaInfo = {
//   [key: string]: Area[];
// };

// const AREA_CONFIG: AreaInfo = JSON.parse(AREA);

type IconProps = {
  className: string;
};

const DownIcon: VFC<IconProps> = ({ className }) => {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      className={className}
      fill="none"
      viewBox="0 0 24 24"
      stroke="currentColor"
    >
      <path
        strokeLinecap="round"
        strokeLinejoin="round"
        strokeWidth={2}
        d="M19 9l-7 7-7-7"
      />
    </svg>
  );
};

type AreaSelectType = {
  category: string;
  areas: { [key: string]: string };
};
const AreaSelect: VFC<AreaSelectType> = ({ category, areas }) => {
  const [value, setValue] = useState<string | null>(null);
  const handleClick = useCallback(
    (e: FormEvent<HTMLButtonElement>) => {
      setValue(e.currentTarget.value);
    },
    [value]
  );
  return (
    <Menu className="inline" key={category} as="div">
      <Menu.Button className="inline-flex relative justify-center items-center py-2 px-2 w-1/2 h-[45px] text-xs font-bold text-black bg-white">
        {value || category}
        <DownIcon className="absolute right-2 w-4 h-4" />
      </Menu.Button>
      <Transition
        enter="transition ease-out duration-100"
        enterFrom="transform opacity-0 scale-95"
        enterTo="transform opacity-100 scale-100"
        leave="transition ease-in duration-75"
        leaveFrom="transform opacity-100 scale-100"
        leaveTo="transform opacity-0 scale-95"
      >
        <Menu.Items className="grid grid-cols-2 pb-3 w-full border-t border-gray-300 border-solid">
          {Object.entries(areas).map(([id, area]) => {
            return (
              <Menu.Item key={id}>
                <button
                  className="col-span-1 py-2 px-2 text-sm text-gray-700 bg-white border-[0.5px] border-gray-200 border-solid"
                  onClick={handleClick}
                  value={area}
                >
                  {area}
                </button>
              </Menu.Item>
            );
          })}
        </Menu.Items>
      </Transition>
    </Menu>
  );
};

const TagSelects = () => {
  const [tags, setTags] = useState<string[]>([]);
  const [inputTag, setInputTag] = useState<string>("");

  const addTagToTags = (addtag: string) => {
    const newTags: string[] = tags.concat();
    newTags.push(addtag);
    setTags(newTags);
    setInputTag("");
  };

  const removeTagFromTags = (removeTag: string) => {
    setTags(
      tags.filter((tag) => {
        if (tag !== removeTag) {
          return tag;
        }
      })
    );
  };
  const changeInputTag = (e: FormEvent<HTMLInputElement>) => {
    setInputTag(e.currentTarget.value);
  };

  return (
    <div className="text-gray-800">
      <div className="flex justify-center">
        <input
          className="py-1.5 px-2 m-3 w-full text-sm font-bold text-left bg-white rounded-lg"
          type="text"
          placeholder="タグ検索"
          value={inputTag}
          onChange={changeInputTag}
        />
        <button
          className="py-1 px-2 my-3 min-w-[60px] text-sm font-bold text-left bg-white rounded-lg"
          onClick={() => {
            addTagToTags(inputTag);
          }}
        >
          追加
        </button>
      </div>
      {tags.map((tag, i) => {
        return (
          <div
            key={i}
            className="inline-flex items-center py-1 px-3 ml-4 text-xs font-bold text-gray-700 bg-white rounded-full border"
          >
            <button
              onClick={() => {
                removeTagFromTags(tag);
              }}
            >
              <XIcon className="w-4" />
            </button>
            {tag}
          </div>
        );
      })}
    </div>
  );
};

const HomePage: VFC = () => {
  return (
    <div className="p-0 m-0 w-full">
      <div className="py-7 font-bold text-white bg-gradient-to-r from-blue-c2 to-blue-c1">
        <p className="pt-4 text-center">プラン検索</p>
        <div className="py-4 mx-auto w-11/12">
          <p className="py-4 text-left">エリア</p>
          <div className="overflow-hidden rounded">
            {Object.entries(AREA).map(([category, value], i) => {
              return <AreaSelect key={i} category={category} areas={value} />;
            })}
          </div>
          <p className=" pt-8 text-left">金額</p>
          <div className="flex gap-3 items-end w-full">
            <input
              className="pl-2 max-w-[130px] h-[40px] text-gray-500 bg-white rounded-xl"
              placeholder="指定なし"
            />
            <p className="leading-[45px]">~</p>
            <input
              className="pl-2 max-w-[130px] h-[40px] text-gray-500 bg-white rounded-xl"
              placeholder="指定なし"
            />
            <p>円</p>
          </div>
          <p className="pt-8 text-left">タグを検索</p>
          <TagSelects />
        </div>
        <button className="flex justify-center items-center mx-auto w-full max-w-[260px] h-[54px] text-center bg-gradient-to-r from-yellow-c2 to-yellow-c1 rounded-md">
          <p className="font-bold text-black">みんなのトリップを検索</p>
          <img
            src="/carry_case_icon.svg"
            width={40}
            height={40}
            alt="キャリーケースのアイコン"
          />
        </button>
      </div>
      <div className="mx-auto w-11/12">
        <p className="py-4 font-bold">新着トリップ</p>
      </div>

      <div className="m-3">
        <PlanContent
          planId={12}
          imgSrc="https://scontent-nrt1-1.cdninstagram.com/v/t51.2885-15/240756876_985417928692918_6974685384653398632_n.jpg?_nc_cat=104&ccb=1-5&_nc_sid=8ae9d6&_nc_ohc=iCO56YT2vYoAX_RaIEl&_nc_ht=scontent-nrt1-1.cdninstagram.com&edm=APCawUEEAAAA&oh=4153bab858eb93077f45740424e6dad8&oe=6149EA1D"
          title="〇〇プラン！"
          description="清水寺と食い倒れたい！そんなあなたにオススメ！貴方が求める旅行がきっとできるはず！"
          tags={["清水寺", "男子会", "楽しい"]}
          price={10000}
          likes={100}
          planner="まっさん"
        />
      </div>

    </div>
  );
};

export default HomePage;
