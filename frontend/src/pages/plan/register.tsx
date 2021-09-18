import { Listbox } from "@headlessui/react";
import { SelectorIcon } from "@heroicons/react/solid";
import imageCompression from "browser-image-compression";
import type { VFC } from "react";
import { useMemo, useRef, useState } from "react";
import { useForm } from "react-hook-form";
import type { Tag } from "react-tag-autocomplete";
import ReactTags from "react-tag-autocomplete";
import type { PathData } from "src/components/Layout";
import { Layout } from "src/components/Layout";
import { ImageUpload } from "src/components/Plan/ImageUpload";
import { StepForm } from "src/components/Plan/StepForm";
import { WatchedInput } from "src/components/Plan/WatchedInput";
import styles from "src/styles/register.module.css";
import { usePostPlan } from "src/utils/hooks/planApi";
import AREA from "src/utils/static/area.json";

type Prefecture = {
  id: string;
  name: string;
};

type RegisterForm = {
  description: string;
  items: [
    {
      description: string;
      finishAt: Date;
      itemOrder: number;
      price: number;
      startAt: Date;
      title: string;
    }
  ];
  regionId: number;
  title: string;
};

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

const pathList: PathData[] = [
  { pathTitle: "マイページ", pathLink: "/user/mypage" },
  { pathTitle: "プラン登録", pathLink: "/plan/register" },
];

const PlanRegisterPage: VFC = () => {
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterForm>();

  const { response, postFn } = usePostPlan();

  const [prefecture, setPrefecture] = useState<Prefecture>(AREA_ARR[0]);
  const [tags, setTags] = useState<Tag[] | []>([]);
  const [thumbnail, setThumbnail] = useState<File | null>(null);

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

  const onSubmit = async (data: RegisterForm): Promise<void> => {
    if (!thumbnail) return;
    // 画像を送信できるようにFormDataに変換する
    const compressOptions = {
      // 1MB以下に圧縮する
      maxSizeMB: 1,
    };
    const compFile = await imageCompression(thumbnail, compressOptions);
    const attachment = {
      content: await imageCompression.getDataUrlFromFile(compFile),
      fileName: thumbnail.name,
    };

    const strTags: string[] = Object.values(tags[0]);

    const params = {
      ...data,
      attachment,
      tags: strTags,
      regionId: 2,
    };

    postFn(params);
  };
  const handleMakeTag = (tag: string) => {
    const formatTag = { id: 0, name: tag };
    setTags([...tags, formatTag]);
  };

  console.log(response);
  return (
    <Layout title="プラン登録" pathList={pathList}>
      <div className="py-4 mx-auto w-full max-w-[330px] text-sm font-bold">
        <div>
          <ImageUpload
            name="サムネイル画像"
            image={thumbnail}
            setImage={setThumbnail}
          />
        </div>
        <div className="py-5">
          <p className="pt-6 pb-2">タイトル</p>
          <input {...register("title")} className={`h-9 ${inputStyle}`} />

          <p className="pt-6 pb-2">都道府県</p>
          <Listbox
            {...register("regionId")}
            value={prefecture}
            onChange={setPrefecture}
          >
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
                    {...register("regionId")}
                    key={area.id}
                    value={area.id}
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
          <WatchedInput onClick={handleMakeTag} />

          <p className="pt-8 pb-2">説明文</p>
          <textarea
            {...register("description")}
            className={`min-h-[120px] ${inputStyle}`}
          />
        </div>
        <StepForm register={register} errors={errors} />
      </div>
      <button className="w-full h-12 bg-white" onClick={handleSubmit(onSubmit)}>
        登録
      </button>
    </Layout>
  );
};

export default PlanRegisterPage;
