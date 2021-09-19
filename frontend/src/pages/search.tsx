import { Listbox } from "@headlessui/react";
import { SelectorIcon, XIcon } from "@heroicons/react/solid";
import { useRouter } from "next/router";
import type { FormEvent, VFC } from "react";
import { useEffect, useState } from "react";
import type { PathData } from "src/components/Layout";
import { Layout } from "src/components/Layout";
import AREA from "src/utils/static/area.json";

import { PlanContent } from "../organisms/PlanContent";
import type { GetSearchPlanParam } from "../utils/hooks/planApi";
import { useGetAllPlans } from "../utils/hooks/planApi";

const inputStyle =
  "font-normal rounded border border-solid border-gray-300 w-full";

const AREA_ARR = Object.values(AREA)
  .map((v) => {
    return Object.entries(v).map(([, value]) => {
      return value;
    });
  })
  .flat();

const findRegionId = (prefecture: string) => {
  const id: number = AREA_ARR.findIndex((ARR) => {
    return ARR === prefecture;
  });
  return id !== -1 ? (id + 1).toString() : "";
};

const pathList: PathData[] = [{ pathTitle: "検索結果", pathLink: "/search" }];

const SearchPage: VFC = () => {
  const [prefecture, setPrefecture] = useState<string>("東京都");
  const [minPrice, setMinPrice] = useState<string>("");
  const [maxPrice, setMaxPrice] = useState<string>("");
  const [tags, setTags] = useState<string[]>([]);
  const [inputTag, setInputTag] = useState<string>("");

  const { getFn, response, loading } = useGetAllPlans();

  const router = useRouter();

  useEffect(() => {
    if (!router.query) {
      return;
    }
    const query = router.query;

    let routerMaxPrice = "";
    let routerPrefecture = "";
    let routerTag: string[] = [];

    if (query.prefecture && typeof query.prefecture === "string") {
      setPrefecture(query.prefecture);
      routerPrefecture = query.prefecture;
    }
    if (query.minPrice && typeof query.minPrice === "string")
      setMinPrice(query.minPrice);
    if (query.maxPrice && typeof query.maxPrice === "string") {
      setMaxPrice(query.maxPrice);
      routerMaxPrice = query.maxPrice;
    }
    if (query.tags && typeof query.tags !== "string") {
      setTags(query.tags);
      routerTag = query.tags;
    }
    if (query.tags && typeof query.tags === "string") {
      setTags([query.tags]);
      routerTag = [query.tags];
    }

    const searchQuery: GetSearchPlanParam = {
      maxPrice: routerMaxPrice,
      regionId: findRegionId(routerPrefecture),
      tag: routerTag.join(","),
      userId: "",
    };

    getFn(searchQuery);
  }, [router.query]);

  useEffect(() => {
    const searchQuery: GetSearchPlanParam = {
      maxPrice: maxPrice,
      regionId: findRegionId(prefecture),
      tag: tags.join(","),
      userId: "",
    };
    getFn(searchQuery);
  }, [maxPrice, prefecture, tags]);

  const changeMinPrice = (e: FormEvent<HTMLInputElement>) => {
    setMinPrice(e.currentTarget.value);
  };

  const changeMaxPrice = (e: FormEvent<HTMLInputElement>) => {
    setMaxPrice(e.currentTarget.value);
  };

  const changeInputTag = (e: FormEvent<HTMLInputElement>) => {
    setInputTag(e.currentTarget.value);
  };

  const addTagToTags = (addtag: string) => {
    if (!addtag) {
      return;
    }
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

  return (
    <Layout title="検索結果" pathList={pathList}>
      <main>
        <div>
          <div className="pt-6 pb-3 text-center bg-gradient-to-r shadow-lg from-blue-c2 to-blue-c1">
            <div>
              <Listbox value={prefecture} onChange={setPrefecture}>
                <Listbox.Button
                  className={`relative h-9 bg-white rounded-xl w-3/4 text-left ${inputStyle}`}
                >
                  <span className="block pl-2 text-sm font-bold leading-9 truncate">
                    {prefecture}
                  </span>
                  <span className="flex absolute inset-y-0 right-0 items-center pr-2 pointer-events-none">
                    <SelectorIcon
                      className="w-5 h-5 text-gray-400"
                      aria-hidden="true"
                    />
                  </span>
                </Listbox.Button>
                <Listbox.Options className="grid overflow-y-scroll grid-cols-3 w-full h-28 bg-white">
                  {AREA_ARR.map((area, i) => {
                    return (
                      <Listbox.Option
                        key={i}
                        value={area}
                        className="col-span-1 h-8 font-normal text-center border-gray-300 border-solid border-[0.1px]"
                      >
                        <p className="pt-1 text-sm font-bold text-gray-700">
                          {area}
                        </p>
                      </Listbox.Option>
                    );
                  })}
                </Listbox.Options>
              </Listbox>
            </div>

            <div className="flex justify-center">
              <input
                className="py-1.5 px-2 m-3 w-1/4 text-sm font-bold text-right rounded-lg"
                type="text"
                placeholder="指定なし"
                value={minPrice}
                onChange={changeMinPrice}
              />
              <p className="pt-2.5 text-xl text-white">~</p>
              <input
                className="py-1.5 px-2 m-3 w-1/4 text-sm font-bold text-right rounded-lg"
                type="text"
                placeholder="指定なし"
                value={maxPrice}
                onChange={changeMaxPrice}
              />
              <p className="pt-6 text-sm text-white">円</p>
            </div>

            <div className="flex justify-center">
              <input
                className="py-1.5 px-2 m-3 w-7/12 text-sm font-bold text-left bg-white rounded-lg"
                type="text"
                placeholder="タグ検索"
                value={inputTag}
                onChange={changeInputTag}
              />
              <button
                className="py-1 px-2 my-3 text-sm font-bold text-left bg-white rounded-lg"
                onClick={() => {
                  addTagToTags(inputTag);
                }}
              >
                追加
              </button>
            </div>

            <div className="">
              {tags &&
                tags.map((tag, i) => {
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
          </div>

          <p className="m-4 text-lg font-bold text-center">検索結果</p>

          {!loading && (
            <div className="m-3">
              {response?.tripPlans.map((plan, i) => {
                return (
                  <PlanContent
                    key={i}
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
      </main>
    </Layout>
  );
};

export default SearchPage;
