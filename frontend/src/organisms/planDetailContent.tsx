import "react-responsive-carousel/lib/styles/carousel.min.css";

import type { VFC } from "react";
import { useEffect } from "react";
import { Carousel } from "react-responsive-carousel";

type Props = {
  place: string;
  order: number;
  description: string;
};

export const PlanDetailContent: VFC<Props> = (props) => {
  const dummyImageUrl =
    "https://scontent-nrt1-1.cdninstagram.com/v/t51.2885-15/240756876_985417928692918_6974685384653398632_n.jpg?_nc_cat=104&ccb=1-5&_nc_sid=8ae9d6&_nc_ohc=iCO56YT2vYoAX_RaIEl&_nc_ht=scontent-nrt1-1.cdninstagram.com&edm=APCawUEEAAAA&oh=4153bab858eb93077f45740424e6dad8&oe=6149EA1D";

  useEffect(() => {
    //instagram の api をたたいて検索
  }, []);

  return (
    <main>
      <div className="my-6 mx-12">
        <div className="flex justify-between">
          <p className="text-3xl font-bold">{props.place}</p>
          <div className="flex text-gray-500">
            <p className=" text-xl font-bold">Spot</p>
            <p className="text-6xl font-bold">0{props.order}</p>
          </div>
        </div>

        <div className="my-2">
          <p>{props.description}</p>
        </div>

        <Carousel>
          <div>
            <img
              src={dummyImageUrl}
              alt="インスタの画像1"
              width={400}
              height={300}
            />
            <p>タイトル１</p>
          </div>
          <div>
            <img
              src={dummyImageUrl}
              alt="インスタの画像2"
              width={400}
              height={300}
            />
            <p>タイトル２</p>
          </div>
          <div>
            <img
              src={dummyImageUrl}
              alt="インスタの画像3"
              width={400}
              height={300}
            />
            <p>タイトル３</p>
          </div>
        </Carousel>

        <div className="">
          <p className="text-sm text-right text-gray-700">instagramより</p>
        </div>
      </div>
    </main>
  );
};
