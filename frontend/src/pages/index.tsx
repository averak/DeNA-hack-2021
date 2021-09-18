import { Menu, Transition } from "@headlessui/react";
import Image from "next/image";
import type { VFC } from "react";
import { Fragment } from "react";
import AREA from "src/utils/static/area.json";

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

const SearchIcon: VFC<IconProps> = ({ className }) => {
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
        strokeWidth="2"
        d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
      />
    </svg>
  );
};

const AreaSelects: VFC = () => {
  return (
    <Menu as="div" className="w-full">
      {Object.entries(AREA).map(([category, value]) => {
        return (
          <div className="inline w-1/2" key={category}>
            <Menu.Button className="inline-flex justify-between items-center py-2 px-2 w-full text-xs font-bold text-black bg-white">
              {category}
              <DownIcon className="w-4 h-4" />
            </Menu.Button>
            <Transition
              as={Fragment}
              enter="transition ease-out duration-100"
              enterFrom="transform opacity-0 scale-95"
              enterTo="transform opacity-100 scale-100"
              leave="transition ease-in duration-75"
              leaveFrom="transform opacity-100 scale-100"
              leaveTo="transform opacity-0 scale-95"
            >
              <Menu.Items className="py-3 px-2 w-full">
                {Object.entries(value).map(([id, area]) => {
                  return (
                    <Menu.Item key={id}>
                      {({ active }) => {
                        return (
                          <button
                            className={`${
                              active
                                ? "bg-violet-500 text-white"
                                : "text-gray-900"
                            } group flex rounded-md items-center w-full px-2 py-2 text-sm`}
                          >
                            {area}
                          </button>
                        );
                      }}
                    </Menu.Item>
                  );
                })}
              </Menu.Items>
            </Transition>
          </div>
        );
      })}
    </Menu>
  );
};

const HomePage: VFC = () => {
  return (
    <div className="p-0 m-0 w-full">
      <div className="py-7 font-bold text-white bg-gradient-to-r from-blue-c2 to-blue-c1">
        <p className="pt-4 text-center">プラン検索</p>
        <div className="mx-auto w-11/12">
          <p className="text-left">エリア</p>
          <AreaSelects />
          <p className="text-left">金額</p>
          <div className="flex gap-3 items-end w-full">
            <input
              className="max-w-[130px] h-[45px] text-gray-500 bg-white rounded-xl"
              placeholder="指定なし"
            />
            <p className="leading-[45px]">~</p>
            <input
              className="max-w-[130px] h-[45px] text-gray-500 bg-white rounded-xl"
              placeholder="指定なし"
            />
            <p>円</p>
          </div>
          <p className="text-left">タグを検索</p>
          <div className="relative w-full h-[40px] bg-white rounded-xl">
            <input
              className="pl-5 leading-[40px] text-gray-600 rounded-xl outline-none"
              placeholder="タグ名を入力"
            />
            <SearchIcon className="absolute right-2 bottom-2 w-4 h-4 text-gray-400" />
          </div>
        </div>
        <button className="flex justify-center items-center mx-auto w-full max-w-[260px] h-[54px] text-center bg-gradient-to-r from-yellow-c2 to-yellow-c1 rounded-md">
          <p className="font-bold text-black">みんなのトリップを検索</p>
          <Image
            src="/carry_case_icon.svg"
            width={40}
            height={40}
            alt="キャリーケースのアイコン"
          />
        </button>
      </div>
    </div>
  );
};

export default HomePage;
