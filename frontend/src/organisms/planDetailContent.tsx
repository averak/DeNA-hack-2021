import "react-responsive-carousel/lib/styles/carousel.min.css";

import type { VFC } from "react";
import { useEffect, useState } from "react";
import { Carousel } from "react-responsive-carousel";

import { useGetTopImageUrl } from "../utils/hooks/instagramApi";

type Props = {
  place: string;
  order: number;
  description: string;
};

export const PlanDetailContent: VFC<Props> = (props) => {
  const { getTopImageFn, response, error, loading } = useGetTopImageUrl();
  const [instaImages, setInstaImages] = useState<string[]>([]);

  useEffect(() => {
    getTopImageFn(props.place);
  }, []);

  useEffect(() => {
    if (!response) {
      return;
    }
    setInstaImages(response);
  }, [response]);

  useEffect(() => {
    if (!error) {
      return;
    }
  }, [error]);

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

        {!loading && (
          <Carousel>
            <div>
              <img
                src={instaImages[0]}
                alt="インスタの画像1"
                width={400}
                height={300}
              />
              <p></p>
            </div>
            <div>
              <img
                src={instaImages[1]}
                alt="インスタの画像2"
                width={400}
                height={300}
              />
              <p></p>
            </div>
            <div>
              <img
                src={instaImages[2]}
                alt="インスタの画像3"
                width={400}
                height={300}
              />
              <p></p>
            </div>
          </Carousel>
        )}

        <div className="">
          <p className="text-sm text-right text-gray-700">instagramより</p>
        </div>
      </div>
    </main>
  );
};
