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
  "font-normal rounded border border-solid border-gray-300 w-full bg-blue-c3 text-gray-800 pl-2 text-base";

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
  { id: 7, name: "明日の夜" },
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

  const { postFn } = usePostPlan();

  const [prefecture, setPrefecture] = useState<Prefecture>(AREA_ARR[0]);
  const [tags, setTags] = useState<Tag[] | []>([]);
  const [thumbnail, setThumbnail] = useState<File | null>(null);

  const reactTagsRef = useRef(null);

  const reactTagsClassNames = useMemo(() => {
    return {
      root: styles.tagInput,
      rootFocused: "",
      selected: "",
      selectedTag:
        "w-20 h-8 mr-1 rounded-lg bg-white border border-solid border-gray-300",
      selectedTagName:
        "bg-gradient-to-r font-bold from-blue-c2 to-blue-c1 text-transparent bg-clip-text",
      search: "",
      searchWrapper:
        "font-normal rounded border border-solid border-gray-300 w-full h-9 mt-2",
      searchInput: "pl-2",
      suggestions: "text-sm border-b border-solid border-gray-300",
      suggestionActive: "",
      suggestionDisabled: "w-4 h-4",
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
      regionId: Number(data.regionId),
      attachment,
      tags: strTags,
    };

    postFn(params);
  };
  const handleMakeTag = (tag: string) => {
    const formatTag = { id: 0, name: tag };
    setTags([...tags, formatTag]);
  };

  return (
    <Layout title="プラン登録" pathList={pathList}>
      <div className="py-12">
        <div className="pt-4 pb-10 mx-auto w-full max-w-[330px] text-sm font-bold">
          <div>
            <ImageUpload
              name="サムネイル画像"
              image={thumbnail}
              setImage={setThumbnail}
            />
          </div>
          <div className="py-5">
            <p className="pt-6 pb-2">タイトル</p>
            <input
              {...register("title", {
                required: true,
                minLength: 4,
                maxLength: 100,
              })}
              className={`h-9 ${inputStyle}`}
            />
            {errors.title?.type == "required" && (
              <div className="py-1 text-xs text-red-500">
                タイトルは必須です
              </div>
            )}
            {errors.title?.type == "minLength" && (
              <div className="py-1 text-xs text-red-500">
                4文字以上で入力してください
              </div>
            )}
            {errors.title?.type == "maxLength" && (
              <div className="py-1 text-xs text-red-500">
                100字以内で入力してくださ
              </div>
            )}

            <p className="pt-6 pb-2">都道府県</p>
            <Listbox value={prefecture} onChange={setPrefecture}>
              <Listbox.Button
                className={`relative h-9 text-left ${inputStyle}`}
              >
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
                      {...register("regionId", { required: true })}
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

            <p className="pt-10 pb-2">
              タグ設定
              <span className="pl-1 text-xs font-normal">
                (タグをもう一度タップすると取り消されます)
              </span>
            </p>
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

            <p className="pt-10 pb-2">説明文</p>
            <textarea
              {...register("description", {
                required: true,
                minLength: 30,
                maxLength: 1000,
              })}
              className={`min-h-[120px] ${inputStyle}`}
            />
            {errors.description?.type == "required" && (
              <div className="py-1 text-xs text-red-500">説明文は必須です</div>
            )}
            {errors.description?.type == "minLength" && (
              <div className="py-1 text-xs text-red-500">
                30字以上で入力してください
              </div>
            )}
            {errors.description?.type == "maxLength" && (
              <div className="py-1 text-xs text-red-500">
                1000字以内で入力してください
              </div>
            )}
          </div>
          <StepForm register={register} errors={errors.items} />
        </div>
        <button
          className="block mx-auto w-[280px] h-14 text-lg font-bold text-gray-800 bg-gradient-to-br from-[#e8c246] to-[#e3e846] rounded-lg"
          onClick={handleSubmit(onSubmit)}
        >
          プランを公開する
        </button>
      </div>
    </Layout>
  );
};

export default PlanRegisterPage;
