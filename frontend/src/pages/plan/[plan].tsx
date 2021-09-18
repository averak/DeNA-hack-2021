import { HeartIcon as HeartIconOutLine } from "@heroicons/react/outline";
import { HeartIcon as HeartIconSolid } from "@heroicons/react/solid";
import { useRouter } from "next/router";
import type { VFC } from "react";
import { useEffect } from "react";
import { useState } from "react";

import type { PathData } from "../../components/Layout";
import { Layout } from "../../components/Layout";
import { PlanDetailContent } from "../../organisms/planDetailContent";

const PlanPage: VFC = () => {
  const router = useRouter();

  const [planId, setPlanId] = useState<number>(0);
  const [eyeCatchUrl, setEyeCatchUrl] = useState<string>("/dummy");
  const [title, setTitle] = useState<string>("");
  const [user, setUser] = useState<string>("");
  const [likeCount, setLikeCount] = useState<number>(0);
  const [isMyLike, setIsMyLike] = useState<boolean>(false);
  const [description, setDescription] = useState<string>("");
  const [tags, setTags] = useState<string[]>([]);
  const [price, setPrice] = useState<number>(0);

  const dummyImageUrl =
    "https://scontent-nrt1-1.cdninstagram.com/v/t51.2885-15/240756876_985417928692918_6974685384653398632_n.jpg?_nc_cat=104&ccb=1-5&_nc_sid=8ae9d6&_nc_ohc=iCO56YT2vYoAX_RaIEl&_nc_ht=scontent-nrt1-1.cdninstagram.com&edm=APCawUEEAAAA&oh=4153bab858eb93077f45740424e6dad8&oe=6149EA1D";

  useEffect(() => {
    // 本来は api をたたく
    setEyeCatchUrl(dummyImageUrl);
    setTitle("清水寺へ行こう！");
    setUser("まっさん");
    setLikeCount(100);
    setIsMyLike(false);
    setDescription(
      "清水寺と食い倒れたい！そんなあなたにオススメ！貴方が求める旅行がきっとできるはず！"
    );
    setTags(["清水寺", "ご飯", "何日目？"]);
    setPrice(10000);

    if (router.query.plan && typeof router.query.plan === "string") {
      setPlanId(parseInt(router.query.plan));
    }
  }, [router.query.plan]);

  const ShowMyLike = () => {
    return isMyLike ? (
      <HeartIconSolid className="w-6" />
    ) : (
      <HeartIconOutLine className="w-6" />
    );
  };

  const ChangeMyLike = () => {
    isMyLike ? setLikeCount(likeCount - 1) : setLikeCount(likeCount + 1);
    setIsMyLike(!isMyLike);
  };

  // とりあえずテンプレート的に用意
  const pathLinkData: PathData[] = [
    { pathTitle: "検索結果", pathLink: "/search" },
    { pathTitle: title, pathLink: "/plan/" + planId },
  ];

  return (
    <main>
      <Layout title="プラン詳細" pathList={pathLinkData}>
        <img src={eyeCatchUrl} alt="アイキャッチ" width={400} height={300} />
        <div className="mb-5">
          <div className="flex justify-between">
            <p className="my-2 ml-4 text-2xl">{title}</p>
            <button
              className="flex justify-center my-2.5 mr-6 text-xl text-gray-600"
              onClick={ChangeMyLike}
            >
              <ShowMyLike />
              <p>{likeCount}</p>
            </button>
          </div>
          <p className="ml-4 text-gray-500">user: @{user}</p>
          <p className="mx-4 mt-4 mb-2">{description}</p>

          <div className="flex ml-4 text-blue-500">
            {/* tag 検索をするので、ここは後に変える */}
            {tags.map((tag, i) => {
              return (
                <p key={i} className="mr-1">
                  #{tag}
                </p>
              );
            })}
          </div>
          <p className="mt-2 ml-4 text-sm text-gray-800">予算: {price} 円</p>
        </div>

        {/* 今はべた書き。api が出来てきたら随時調整 */}
        <div>
          <p className="text-2xl font-bold text-center">１日目</p>

          {/* 今はべた書き。api が出来てきたら随時調整 */}
          <PlanDetailContent
            order={1}
            place="嵐山"
            description="嵐山は嵐山って感じで素敵です！食べ物も美味しくて最高でした"
          />
          <PlanDetailContent
            order={2}
            place="嵐山"
            description="嵐山は嵐山って感じで素敵です！食べ物も美味しくて最高でした"
          />
          <PlanDetailContent
            order={3}
            place="嵐山"
            description="嵐山は嵐山って感じで素敵です！食べ物も美味しくて最高でした"
          />
        </div>

        <div>
          <p className="text-2xl font-bold text-center">２日目</p>

          {/* 今はべた書き。api が出来てきたら随時調整 */}
          <PlanDetailContent
            order={1}
            place="嵐山"
            description="嵐山は嵐山って感じで素敵です！食べ物も美味しくて最高でした"
          />
          <PlanDetailContent
            order={2}
            place="嵐山"
            description="嵐山は嵐山って感じで素敵です！食べ物も美味しくて最高でした"
          />
          <PlanDetailContent
            order={3}
            place="嵐山"
            description="嵐山は嵐山って感じで素敵です！食べ物も美味しくて最高でした"
          />
        </div>
      </Layout>
    </main>
  );
};

export default PlanPage;
