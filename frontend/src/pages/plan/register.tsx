import { Listbox } from "@headlessui/react";
import { SelectorIcon } from "@heroicons/react/solid";
import type { VFC } from "react";
import { useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import type { Tag } from "react-tag-autocomplete";
import ReactTags from "react-tag-autocomplete";
import type { PathData } from "src/components/Layout";
import { Layout } from "src/components/Layout";
import { StepFormWrapper } from "src/components/Plan/StepFormWrapper";
import styles from "src/styles/register.module.css";
import AREA from "src/utils/static/area.json";

const inputStyle =
  "font-normal rounded border border-solid border-gray-300 w-full";

const AREA_ARR = Object.values(AREA)
  .map((v) => {
    return Object.entries(v).map(([key, value]) => {
      return { id: key, name: value };
    });
  })
  .flat();

const suggestion = [
  { id: 3, name: "Bananas" },
  { id: 4, name: "昨日の朝" },
  { id: 5, name: "あいうえお" },
  { id: 6, name: "明日の朝" },
];

type Prefecture = {
  id: string;
  name: string;
};

const pathList: PathData[] = [
  { pathTitle: "マイページ", pathLink: "/user/mypage" },
  { pathTitle: "プラン登録", pathLink: "/plan/register" },
];

const PlanRegisterPage: VFC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm();

  const [prefecture, setPrefecture] = useState<Prefecture>(AREA_ARR[0]);
  const [tags, setTags] = useState<Tag[] | []>([]);

  const reactTagsRef = useRef(null);

  const reactTagsClassNames = useMemo(() => {
    return {
      root: styles.tagInput,
      rootFocused: "",
      selected: "",
      selectedTag: "",
      selectedTagName: "",
      search: "",
      searchWrapper:
        "font-normal rounded border border-solid border-gray-300 w-full h-9",
      searchInput: "",
      suggestions: "",
      suggestionActive: "",
      suggestionDisabled: "",
    };
  }, []);
  const handleDelete = (i: number) => {
    if (!tags) return;
    const tag = tags.slice(0);
    tag.splice(i, 1);
    setTags([...tag]);
  };
  const handleAddition = (tag: Tag) => {
    const addedTag: Tag[] = tags ? [...tags, tag] : [tag];
    setTags([...addedTag]);
  };

  return (
    <Layout title="プラン登録" pathList={pathList}>
      <div className="py-4 mx-auto w-full max-w-[330px] text-sm font-bold">
        <div className="py-5">
          <p className="pt-6 pb-2">タイトル</p>
          <input className={`h-9 ${inputStyle}`} />

          <p className="pt-6 pb-2">都道府県</p>
          <Listbox value={prefecture} onChange={setPrefecture}>
            <Listbox.Button className={`relative h-9 text-left ${inputStyle}`}>
              <span className="block leading-9 truncate">
                {prefecture.name}
              </span>
              <span className="flex absolute inset-y-0 right-0 items-center pr-2 pointer-events-none">
                <SelectorIcon
                  className="w-5 h-5 text-gray-400"
                  aria-hidden="true"
                />
              </span>
            </Listbox.Button>
            <Listbox.Options className="grid overflow-y-scroll grid-cols-3 w-full h-28">
              {AREA_ARR.map((area) => {
                return (
                  <Listbox.Option
                    key={area.id}
                    value={area}
                    className="col-span-1 h-7 font-normal text-center border-[0.1px] border-gray-300 border-solid"
                  >
                    {area.name}
                  </Listbox.Option>
                );
              })}
            </Listbox.Options>
          </Listbox>

          <p className="pt-7 pb-2">タグ設定</p>
          <div className="w-full">
            <ReactTags
              ref={reactTagsRef}
              tags={tags}
              suggestions={suggestion}
              onDelete={handleDelete}
              onAddition={handleAddition}
              classNames={reactTagsClassNames}
            />
          </div>
          <p className="pt-4 text-xs">
            当てはまるタグがない..? タグ作成にご協力ください
          </p>
          <div className="flex gap-2 pl-3 h-8">
            <input className={`${inputStyle}`} />
            <button className="w-14 bg-gradient-to-r from-yellow-c2 to-yellow-c1" />
          </div>

          <p className="pt-8 pb-2">説明文</p>
          <textarea className={`min-h-[120px] ${inputStyle}`} />
        </div>
      </div>
    </Layout>
  );
};

export default PlanRegisterPage;
