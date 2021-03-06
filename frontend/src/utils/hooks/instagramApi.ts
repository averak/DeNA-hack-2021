import type { AxiosResponse } from "axios";
import axios from "axios";
import { useCallback, useState } from "react";

const instagramURL = process.env.NEXT_PUBLIC_IG_URI;
const instagramUserId = process.env.NEXT_PUBLIC_IG_ACCOUNT_ID;
const instagramAccessToken = process.env.NEXT_PUBLIC_IG_ACCESS_TOKEN;

const instaPhotoCount = 3;

export type TagIdResponse = {
  data: { id: string }[];
};

export type TopImageResponse = {
  data: TopImageData[];
};

export type TopImageData = {
  id: string;
  media_type: string;
  media_url: string;
  parmalink: string;
};

// ハッシュタグからハッシュタグIDを取得
const getTagId = (tag: string): Promise<AxiosResponse<TagIdResponse>> => {
  const url = `${instagramURL}/ig_hashtag_search?user_id=${instagramUserId}&q=${tag}&access_token=${instagramAccessToken}`;
  return axios.get<TagIdResponse>(url);
};

// ハッシュタグから最高評価の画像のurlを取得
export const useGetTopImageUrl = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string[] | null>(null);
  const [error] = useState<Error | null>(null);

  const getTopImageFn = useCallback(async (tag: string) => {
    setLoading(true);
    await getTagId(tag)
      .then((res) => {
        const tagId = res.data.data[0].id;
        const url = `${instagramURL}/${tagId}/top_media?user_id=${instagramUserId}&fields=id,media_type,media_url,permalink&access_token=${instagramAccessToken}`;
        axios
          .get<TopImageResponse>(url)
          .then((res) => {
            const datas: TopImageData[] = res.data.data;
            const images = datas.filter((d) => {
              return d.media_type === "IMAGE";
            });
            if (images.length >= instaPhotoCount) {
              setResponse([
                images[0].media_url,
                images[1].media_url,
                images[2].media_url,
              ]);
            } else {
              const imgArray: string[] = [];
              for (const img of images) {
                imgArray.push(img.media_url);
              }
              for (let i = 0; i < instaPhotoCount - imgArray.length; i++) {
                // 足りない分no-image
                imgArray.push("/noimage.png");
              }
              setResponse(imgArray);
            }
          })
          .catch((err) => {
            console.error(err);
            setResponse(["/noimage.png", "/noimage.png", "/noimage.png"]);
          })
          .finally(() => {
            setLoading(false);
          });
      })
      .catch((err) => {
        console.error(err);
        setResponse(["/noimage.png", "/noimage.png", "/noimage.png"]);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, getTopImageFn };
};
