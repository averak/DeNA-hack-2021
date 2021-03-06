import { Menu, Transition } from "@headlessui/react";
import { XIcon } from "@heroicons/react/solid";
import Link from "next/link";
import type { Dispatch, FormEvent, SetStateAction, VFC } from "react";
import { useEffect } from "react";
import { useCallback, useState } from "react";
import { Layout } from "src/components/Layout";
import { useGetAllPlans } from "src/utils/hooks/planApi";
import AREA from "src/utils/static/area.json";

import { PlanContent } from "../organisms/PlanContent";

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
  setPrefectureFn: Dispatch<SetStateAction<string>>;
};
const AreaSelect: VFC<AreaSelectType> = ({
  category,
  areas,
  setPrefectureFn,
}) => {
  const [value] = useState<string | null>(null);
  const handleClick = useCallback(
    (e: FormEvent<HTMLButtonElement>) => {
      setPrefectureFn(e.currentTarget.value);
    },
    [setPrefectureFn]
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

type TagSelectsProps = {
  setValue: Dispatch<SetStateAction<string[]>>;
};

const TagSelects: VFC<TagSelectsProps> = ({ setValue }) => {
  const [tags, setTags] = useState<string[]>([]);
  const [inputTag, setInputTag] = useState<string>("");

  const addTagToTags = (addtag: string) => {
    if (!addtag) return;

    const newTags: string[] = tags.concat();
    newTags.push(addtag);
    setTags(newTags);
    setValue(newTags);
    setInputTag("");
  };

  const removeTagFromTags = (removeTag: string) => {
    const newTags = tags.filter((tag) => {
      if (tag !== removeTag) {
        return tag;
      }
    });
    setTags(newTags);
    setValue(newTags);
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
          placeholder="????????????"
          value={inputTag}
          onChange={changeInputTag}
        />
        <button
          className="py-1 px-2 my-3 min-w-[60px] text-sm font-bold text-left bg-white rounded-lg"
          onClick={() => {
            addTagToTags(inputTag);
          }}
        >
          ??????
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
  const [prefecture, setPrefecture] = useState<string>("");
  const [minPrice, setMinPrice] = useState<string>();
  const [maxPrice, setMaxPrice] = useState<string>();
  const [tags, setTags] = useState<string[]>([]);

  const { loading, error, response, getFn } = useGetAllPlans();

  useEffect(() => {
    getFn().then(() => {
      // console.log(response)
    });
  }, []);

  useEffect(() => {
    if (!error) {
      return;
    }
  }, [error]);

  const changeMinPrice = (e: FormEvent<HTMLInputElement>) => {
    setMinPrice(e.currentTarget.value);
  };

  const changeMaxPrice = (e: FormEvent<HTMLInputElement>) => {
    setMaxPrice(e.currentTarget.value);
  };

  return (
    <Layout title="?????????" pathList={[]}>
      <div className="p-0 m-0 w-full">
        <div className="py-7 font-bold text-white bg-gradient-to-r from-blue-c2 to-blue-c1">
          <p className="pt-4 text-center">???????????????</p>
          <div className="py-4 mx-auto w-11/12">
            <div className="flex">
              <p className="py-4 text-left">?????????</p>
              <input
                placeholder="??????????????????"
                value={prefecture}
                className="pl-2 m-3 h-8 font-bold text-gray-700 rounded-lg"
                readOnly
              />
            </div>
            <div className="overflow-hidden rounded">
              {Object.entries(AREA).map(([category, value], i) => {
                return (
                  <AreaSelect
                    key={i}
                    category={category}
                    areas={value}
                    setPrefectureFn={setPrefecture}
                  />
                );
              })}
            </div>
            <p className=" pt-8 text-left">??????</p>
            <div className="flex gap-3 items-end w-full">
              <input
                className="pl-2 max-w-[130px] h-[40px] text-gray-500 bg-white rounded-xl"
                placeholder="????????????"
                value={minPrice}
                onChange={changeMinPrice}
              />
              <p className="leading-[45px]">~</p>
              <input
                className="pl-2 max-w-[130px] h-[40px] text-gray-500 bg-white rounded-xl"
                placeholder="????????????"
                value={maxPrice}
                onChange={changeMaxPrice}
              />
              <p>???</p>
            </div>
            <p className="pt-8 text-left">???????????????</p>
            <TagSelects setValue={setTags} />
          </div>
          <Link
            href={{
              pathname: "/search",
              query: {
                minPrice: minPrice,
                maxPrice: maxPrice,
                tags: tags,
                prefecture: prefecture,
              },
            }}
          >
            <button className="flex justify-center items-center mx-auto w-full max-w-[260px] h-[54px] text-center bg-gradient-to-r from-yellow-c2 to-yellow-c1 rounded-md">
              <p className="font-bold text-black">?????????????????????????????????</p>
              <img
                src="/carry_case_icon.svg"
                width={40}
                height={40}
                alt="????????????????????????????????????"
              />
            </button>
          </Link>
        </div>
        <div className="mx-auto w-11/12">
          <p className="py-4 font-bold">??????????????????</p>
        </div>

        {!loading && (
          <div className="m-3">
            {response?.tripPlans.map((plan, i) => {
              return (
                <PlanContent
                  key={i}
                  place={plan.items[0].title}
                  planId={plan.id}
                  imgSrc="https://scontent-nrt1-1.cdninstagram.com/v/t51.2885-15/240756876_985417928692918_6974685384653398632_n.jpg?_nc_cat=104&ccb=1-5&_nc_sid=8ae9d6&_nc_ohc=iCO56YT2vYoAX_RaIEl&_nc_ht=scontent-nrt1-1.cdninstagram.com&edm=APCawUEEAAAA&oh=4153bab858eb93077f45740424e6dad8&oe=6149EA1D"
                  title={plan.title}
                  description={plan.description}
                  tags={plan.tags}
                  price={plan.items[0].price}
                  likes={plan.likes}
                  planner={plan.author.lastName + plan.author.firstName}
                  isLike={plan.isLiked}
                />
              );
            })}
          </div>
        )}
      </div>
    </Layout>
  );
};

export default HomePage;
