import { HeartIcon as OutlineHeartIcon } from "@heroicons/react/outline"
import { HeartIcon as SolidHeartIcon } from "@heroicons/react/solid"
import axios from "axios";
import Link from "next/link";
import Router from "next/router";
import type { VFC } from "react";
import { useState } from "react";

type PlanProps = {
  planId: number;
  imgSrc: string;
  title: string;
  description: string;
  tags: string[];
  price: number;
  likes: number;
  planner: string;
};

export const PlanContent: VFC<PlanProps> = (props) => {
  const [likes, setLikes] = useState(props.likes);
  const [isMyLike, setIsMyLike] = useState<boolean>(false)
  const postLike = () => {
    axios
      .put(
        `${process.env.NEXT_PUBLIC_BASE_URI}/api/travel_plans/${props.planId}/likes`
      )
      .then(() => {
        setLikes(likes + 1);
      })
      .catch(() => {
        Router.push("_error");
      });
  };

  const ShowMyLike = () => {
    return isMyLike ? <SolidHeartIcon className="mr-1.5 w-5"/> : <OutlineHeartIcon className="mr-1.5 w-5"/>
  }

  const toggleMyLike = () => {
    isMyLike ? setLikes(likes-1) : setLikes(likes+1)
    setIsMyLike(!isMyLike)
  }

  return (
    <div className="pb-2 rounded-2xl border-2">
      <img className="object-cover w-full h-48 rounded-t-2xl" src={props.imgSrc} alt="旅行プランのサムネイルです" />

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
          <p className="m-1 text-sm text-gray-500">planned by {props.planner}さん</p>
        </div>

        {/* 三角形はすぐには出来なそうなのでしてないです...時間を見つけてします...(田中) */}
        <button className="flex justify-center mt-4 w-4/12 h-8 font-bold text-white bg-blue-600 shadow-xl" onClick={toggleMyLike}>
          <ShowMyLike/>
          <p className="mt-1">{likes}</p>
        </button>

      </div>
    </div>
  );
};
