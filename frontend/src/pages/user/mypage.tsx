import { Listbox, Transition } from "@headlessui/react";
import { CheckIcon, SelectorIcon } from "@heroicons/react/solid";
import { useRouter } from "next/router";
import type { VFC } from "react";
import { useEffect } from "react";
import { useState } from "react";
import type { PathData } from "src/components/Layout";
import { Layout } from "src/components/Layout";
import { useGetAllPlans, useGetLikePlans, useGetFiles } from "src/utils/hooks/planApi";
import { PlanContent } from "src/organisms/PlanContent";

import { isAccessToken } from "../../utils/libs/accessToken";

const planOption = [
  { id: 1, name: "いいねしたトリップ" },
  { id: 2, name: "投稿したトリップ" },
];

const MyPage: VFC = () => {
  const router = useRouter();
  // ログインしていなければログインページに遷移
  useEffect(() => {
    if (!isAccessToken()) {
      router.replace("/user/login");
    }
  }, []);

  const likePlansState = useGetLikePlans();

  const allPlansState = useGetAllPlans();


  const [selected, setSelected] = useState(planOption[0]);

  useEffect(() => {
    likePlansState.getFn();
    allPlansState.getFn();
  }, []);

  const pathList: PathData[] = [
    { pathTitle: "マイページ", pathLink: "/user/mypage" },
  ];
  return (
    <Layout title="マイページ" pathList={pathList}>
      <div className="pt-4 mx-auto max-w-[300px]">
        <Listbox value={selected} onChange={setSelected}>
          <Listbox.Button className="relative py-2 pr-10 pl-3 w-full text-left bg-gray-200 rounded-lg cursor-pointer focus:outline-none">
            <span className="block truncate">{selected.name}</span>
            <span className="flex absolute inset-y-0 right-0 items-center pr-2 pointer-events-none">
              <SelectorIcon
                className="w-5 h-5 text-gray-400"
                aria-hidden="true"
              />
            </span>
          </Listbox.Button>
          <Transition
            leave="transition ease-in duration-100"
            leaveFrom="opacity-100"
            leaveTo="opacity-0"
          >
            <Listbox.Options className="overflow-auto absolute py-1 mt-1 w-full max-w-[300px] max-h-60 text-base sm:text-sm bg-white rounded-md ring-1 ring-black ring-opacity-5 shadow-lg focus:outline-none">
              {planOption.map((plan, i) => {
                return (
                  <Listbox.Option
                    key={i}
                    className={({ active }) => {
                      return `${
                        active ? "text-amber-900 bg-amber-100" : "text-gray-900"
                      }
                          cursor-default select-none relative py-2 pl-10`;
                    }}
                    value={plan}
                  >
                    {({ selected, active }) => {
                      return (
                        <>
                          <span
                            className={`${
                              selected ? "font-medium" : "font-normal"
                            } block truncate`}
                          >
                            {plan.name}
                          </span>
                          {selected ? (
                            <span
                              className={`${
                                active ? "text-amber-600" : "text-amber-600"
                              }
                                absolute inset-y-0 left-0 flex items-center pl-3`}
                            >
                              <CheckIcon
                                className="w-5 h-5"
                                aria-hidden="true"
                              />
                            </span>
                          ) : null}
                        </>
                      );
                    }}
                  </Listbox.Option>
                );
              })}
            </Listbox.Options>
          </Transition>
        </Listbox>
      </div>
      <div className="py-6">
          { selected.id == 1 && !likePlansState.loading && (
            likePlansState.response?.tripPlans.map((v,i) => {
              return (
                <PlanContent planId={v.id} imgSrc={v.attachment.fileName} 
              )
            })
          )}
      </div>
    </Layout>
  );
};

export default MyPage;
