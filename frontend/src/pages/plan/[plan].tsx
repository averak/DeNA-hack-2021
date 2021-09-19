import { HeartIcon as HeartIconOutLine } from "@heroicons/react/outline";
import { HeartIcon as HeartIconSolid } from "@heroicons/react/solid";
import { useRouter } from "next/router";
import type { VFC } from "react";
import { useEffect } from "react";
import { useState } from "react";
import { useGetTopImageUrl } from "src/utils/hooks/instagramApi";
import { useGetPlan, usePutLikes } from "src/utils/hooks/planApi";

import type { PathData } from "../../components/Layout";
import { Layout } from "../../components/Layout";
import { PlanDetailContent } from "../../organisms/planDetailContent";

const PlanPage: VFC = () => {
  const topImageUrl = useGetTopImageUrl();
  const { loading, error, response, getFn } = useGetPlan();
  const putLikes = usePutLikes();

  const router = useRouter();

  const [planId, setPlanId] = useState<number>(0);
  const [likes, setLikes] = useState<number>(0);
  const [isLike, setIsLike] = useState<boolean>(false);
  const [price] = useState<number>(0);

  useEffect(() => {
    const routeQuerys = router.query.plan;
    if (routeQuerys !== undefined) {
      let id = -1;
      if (typeof routeQuerys === "string") {
        id = parseInt(routeQuerys);
      } else {
        return;
      }
      setPlanId(id);
      getFn(id).then(() => {
        if (typeof response?.isLiked === "boolean") {
          setIsLike(response?.isLiked);
          topImageUrl.getTopImageFn(response.items[0].title).then();
        }
      });
    }
  }, [getFn, response?.isLiked, router.query.plan]);

  useEffect(() => {
    if (!error) {
      return;
    }
  }, [error]);

  useEffect(() => {
    if (!response) {
      return;
    }
    setIsLike(response.isLiked);
    setLikes(response.likes);
  }, [response]);

  // いいねの数が変化して存在すれば代入
  useEffect(() => {
    if (typeof putLikes.response === "number") {
      setLikes(putLikes.response);
    }
  }, [putLikes.response]);

  const ShowMyLike = () => {
    return isLike ? (
      <HeartIconSolid className="w-6 text-red-500" />
    ) : (
      <HeartIconOutLine className="w-6" />
    );
  };

  const ChangeMyLike = () => {
    if (loading || putLikes.loading) {
      return;
    }
    putLikes.putFn(planId, { isLike: !isLike }).then(() => {
      setIsLike(!isLike);
    });
  };

  const pathLinkData: PathData[] = [
    {
      pathTitle: response?.title ? response.title : "プラン詳細",
      pathLink: "/plan/" + planId,
    },
  ];

  const tappedTag = (tag: string) => {
    const query = router.query;
    const queryTags = query.tags;
    let setArray: string[] = [];
    if (queryTags) {
      if (typeof queryTags === "string") {
        setArray = [tag, queryTags];
        const uniqArray = setArray.filter((x, i, self) => {
          return self.indexOf(x) === i;
        });
        router.push({
          pathname: "/search",
          query: {
            minPrice: "",
            maxPrice: "",
            prefecture: "",
            tags: uniqArray,
          },
        });
      } else {
        queryTags.push(tag);
        setArray = queryTags;
        const uniqArray = setArray.filter((x, i, self) => {
          return self.indexOf(x) === i;
        });
        router.push({
          pathname: "/search",
          query: {
            minPrice: "",
            maxPrice: "",
            prefecture: "",
            tags: uniqArray,
          },
        });
      }
    } else {
      router.push({
        pathname: "/search",
        query: {
          minPrice: "",
          maxPrice: "",
          prefecture: "",
          tags: tag,
        },
      });
    }
  };

  return (
    <main>
      {!(loading && putLikes.loading) && (
        <Layout title={response?.title ?? "プラン詳細"} pathList={pathLinkData}>
          <img
            src={
              topImageUrl.response ? topImageUrl.response[0] : "/noimage.png"
            }
            alt="アイキャッチ"
            width={400}
            height={300}
          />
          <div className="mb-5">
            <div className="flex justify-between">
              <p className="my-2 ml-4 text-2xl">{response?.title}</p>
              <button
                className="flex justify-center my-2.5 mr-6 text-xl text-gray-600"
                onClick={ChangeMyLike}
              >
                <ShowMyLike />
                <p>{likes}</p>
              </button>
            </div>
            <p className="ml-4 text-gray-500">
              user: @
              {response?.author.lastName + " " + response?.author.firstName}さん
            </p>
            <p className="mx-4 mt-4 mb-2">{response?.description}</p>

            <div className="flex ml-4 text-blue-500">
              {/* tag 検索をするので、ここは後に変える */}
              {response?.tags.map((tag, i) => {
                return (
                  <button
                    key={i}
                    className="mr-1"
                    onClick={() => {
                      return tappedTag(tag);
                    }}
                  >
                    #{tag}
                  </button>
                );
              })}
            </div>
            {/* ここにpriceを計算して入れたい */}
            <p className="mt-2 ml-4 text-sm text-gray-800">予算: {price} 円</p>
            {response?.items.map((item, i) => {
              return (
                <PlanDetailContent
                  key={i}
                  place={item.title}
                  order={item.itemOrder}
                  description={item.description}
                />
              );
            })}
          </div>
        </Layout>
      )}
    </main>
  );
};

export default PlanPage;
