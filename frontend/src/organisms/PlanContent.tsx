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
  return (
    <div>
      <div>
        <img src={props.imgSrc} alt="旅行プランのサムネイルです" />
      </div>
      <div>
        <h1>{props.title}</h1>
      </div>
      <div>
        {props.tags.map((tag, i) => {
          return (
            <Link href="/search" key={i}>
              <a>#{tag}</a>
            </Link>
          );
        })}
      </div>
      <div>
        <p>planned by {props.planner}さん</p>
      </div>
      <div>
        <button onClick={postLike}>いいね: {likes}</button>
      </div>
    </div>
  );
};
