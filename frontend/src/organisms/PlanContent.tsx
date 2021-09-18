import { HeartIcon as OutlineHeartIcon } from "@heroicons/react/outline";
import { HeartIcon as SolidHeartIcon } from "@heroicons/react/solid";
import Link from "next/link";
import type { VFC } from "react";
import { useEffect } from "react";
import { useState } from "react";
import { usePutLikes } from "src/utils/hooks/planApi";

type PlanProps = {
  planId: number;
  imgSrc: string;
  title: string;
  description: string;
  tags: string[];
  price: number;
  likes: number;
  planner: string;
  isLike: boolean;
};

export const PlanContent: VFC<PlanProps> = (props) => {
  const { loading, error, response, putFn } = usePutLikes();
  const [likes, setLikes] = useState<number>(props.likes);
  const [isMyLike, setIsMyLike] = useState<boolean>(props.isLike);

  useEffect(() => {
    if (!error) {
      return;
    }
    console.error(error);
  }, [error]);

  // いいねの数が変化して存在すれば代入
  useEffect(() => {
    if (!response) {
      return;
    }
    setLikes(response);
  }, [response]);

  const ShowMyLike = () => {
    return isMyLike ? (
      <SolidHeartIcon className="mr-1.5 w-5 text-red-500" />
    ) : (
      <OutlineHeartIcon className="mr-1.5 w-5" />
    );
  };

  const toggleMyLike = () => {
    if (!loading) {
      putFn(props.planId, { isLike: props.isLike }).then(() => {
        // console.log(res);
      });
      if (response) {
        setLikes(response);
        setIsMyLike(!isMyLike);
      }
    }
  };

  return (
    <div className="pb-2 rounded-2xl border-2">
      <img
        className="object-cover w-full h-48 rounded-t-2xl"
        src={props.imgSrc}
        alt="旅行プランのサムネイルです"
      />

      <div className="flex justify-between">
        <p className="my-3 mx-1.5 text-2xl">{props.title}</p>
        <p className="mx-1.5 mt-5 text-sm">予算: {props.price} 円</p>
      </div>

      <div className="flex justify-between">
        <div>
          <div className="my-1 text-sm text-blue-400">
            {props.tags.map((tag, i) => {
              return (
                <Link href="/search" key={i}>
                  <a className="ml-1">#{tag}</a>
                </Link>
              );
            })}
          </div>
          <p className="m-1 text-sm text-gray-500">
            planned by {props.planner}さん
          </p>
        </div>

        {/* 三角形はすぐには出来なそうなのでしてないです...時間を見つけてします...(田中) */}
        <button
          className="flex justify-center mt-4 w-4/12 h-8 font-bold text-white bg-blue-600 shadow-xl"
          onClick={toggleMyLike}
        >
          <ShowMyLike />
          <p className="mt-1">{likes}</p>
        </button>
      </div>
    </div>
  );
};
