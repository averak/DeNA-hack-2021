import axios from "axios";
import { useCallback, useState } from "react";

import { getTokenHeader } from "../libs/accessToken";

const hostname = process.env.NEXT_PUBLIC_BASE_URI;

export type TripPlanParam = {
  attachment: {
    content: string;
    fileName: string;
  };
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
  tags: string[];
  title: string;
};

export type GetSearchPlanParam = {
  maxPrice: string;
  regionId: string;
  tag: string;
  userId: number;
};

export type UpdatePlanParam = {
  attachment: {
    content: string;
    fileName: string;
  };
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
  regionId: 0;
  tags: string[];
  title: string;
};

export type LikesParam = {
  isLike: boolean;
};

export type TripPlanResponse = {
  attachment: {
    fileName: string;
    uuid: string;
  };
  author: {
    email: string;
    firstName: string;
    id: number;
    lastName: string;
  };
  description: string;
  id: number;
  isLiked: boolean;
  items: TripPlanItem[];
  likes: number;
  regionId: number;
  tags: string[];
  title: string;
};

export type FormatTripPlan = TripPlanResponse & {
  imgUrl: string;
  price: number;
};

export type FormatTripPlans = {
  tripPlans: FormatTripPlan[];
};

export type TripPlanResponses = {
  tripPlans: TripPlanResponse[];
};

export type TripDetailResponse = {
  id: number;
  title: string;
  description: string;
  author: { firstName: string; lastName: string };
  regionId: number;
  likes: number;
  tags: string[];
  attachmentUrl: string;
  items: TripPlanItem[];
};

export type TripPlanItem = {
  description: string;
  itemOrder: number;
  price: number;
  startAt: Date;
  finishAt: Date;
  title: string;
};

export type Attachment = {
  description: string;
  filename: string;
  inputStream: Blob;
  open: boolean;
  readable: boolean;
  uri: string;
  url: string;
};

const decodeBinary = (binary: Blob): Promise<string> => {
  return new Promise((resolve, reject) => {
    const reader = new FileReader();
    // Sets up even listeners BEFORE you call reader.readAsDataURL
    reader.onload = function () {
      const result = reader.result as string;
      return resolve(result);
    };

    reader.onerror = function (error) {
      return reject(error);
    };
    // Calls reader function
    reader.readAsDataURL(binary);
  });
};

// 画像のuuidから画像pathを取得
const getImgSrc = async (uuid: string) => {
  const url = `${hostname}/api/trip_plans/attachments/${uuid}`;
  const imgUrl = await axios
    .get<Blob>(url, {
      headers: { Authorization: getTokenHeader() },
      responseType: "blob",
    })
    .then(async (res) => {
      // const blobUrl = window.URL.createObjectURL(new Blob([res.data]));
      // console.log(blobUrl);
      const b64 = await decodeBinary(res.data);
      // const sp = b64.slice(b64.indexOf(",") + 1).split("base64");
      // const prefix = sp[0].split("data").join("data:");
      // return `${prefix};base64,${sp[1]}`;
      // return b64.slice(b64.indexOf(",") + 1);
      return b64;
    });
  return imgUrl;
};

const sumPrice = (prices: number[]) => {
  return prices.reduce((prev, current) => {
    return prev + current;
  });
};

// タグ一覧の取得
export const useGetTags = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string[] | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    const url = `${hostname}/api/tags`;
    await axios
      .put<string[]>(url, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseData = await res.data;
        setResponse(responseData);
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, getFn };
};

// 全てのプランの取得
export const useGetAllPlans = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<FormatTripPlans | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async (params?: GetSearchPlanParam) => {
    setLoading(true);

    const query = [];
    if (params) {
      if (params.maxPrice) query.push("maxPrice=" + params.maxPrice);
      if (params.regionId) query.push("regionId=" + params.regionId);
      if (params.tag) query.push("tag=" + params.tag);
      if (params.userId) query.push("userId=" + params.userId);
    }

    let url = `${hostname}/api/trip_plans`;
    if (query.length) {
      const queryString = "?" + query.join("&");
      url = url + queryString;
    }

    await axios
      .get<TripPlanResponses>(url, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseData = await res.data;
        const planData: FormatTripPlan[] = await Promise.all(
          responseData.tripPlans.map(async (tripPlan) => {
            const imgUrl = await getImgSrc(tripPlan.attachment.uuid);
            const price = sumPrice(
              tripPlan.items.map((v) => {
                return v.price;
              })
            );
            return {
              ...tripPlan,
              imgUrl,
              price,
            };
          })
        );
        setResponse({ tripPlans: planData });
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, getFn };
};

// プラン作成
export const usePostPlan = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const postFn = useCallback(async (params: TripPlanParam) => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans`;
    await axios
      .post(url, params, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseStatusCode = await res.status;
        if (responseStatusCode === 200) {
          setResponse("success");
        }
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, postFn };
};

// idでプラン取得
export const useGetPlan = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<FormatTripPlan | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async (tripPlanId: number) => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans/${tripPlanId}`;
    await axios
      .get<TripPlanResponse>(url, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseData = await res.data;
        const imgUrl = await getImgSrc(responseData.attachment.uuid);
        const price = sumPrice(
          responseData.items.map((v) => {
            return v.price;
          })
        );
        setResponse({
          ...responseData,
          imgUrl,
          price,
        });
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, getFn };
};

// プランの更新
export const usePutPlan = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const putFn = useCallback(
    async (tripPlanId: number, params: UpdatePlanParam) => {
      setLoading(true);
      const url = `${hostname}/api/trip_plans/${tripPlanId}`;
      await axios
        .put<number>(url, params, {
          headers: { Authorization: getTokenHeader() },
        })
        .then(async (res) => {
          const responseStatusCode = await res.status;
          if (responseStatusCode === 200) {
            setResponse("success");
          }
        })
        .catch((err) => {
          console.error(err);
          setError(err);
        })
        .finally(() => {
          setLoading(false);
        });
    },
    []
  );
  return { loading, error, response, putFn };
};

// プラン削除
export const useDeletePlan = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const deleteFn = useCallback(async (tripPlanId: number) => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans/${tripPlanId}`;
    await axios
      .delete(url, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseStatusCode = await res.status;
        if (responseStatusCode === 200) {
          setResponse("success");
        }
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, deleteFn };
};

// プランにいいねを押す(現在のいいね数を返す)
export const usePutLikes = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<number | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const putFn = useCallback(async (tripPlanId: number, params: LikesParam) => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans/${tripPlanId}/likes`;
    await axios
      .put<{ likes: number }>(url, params, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseNum = await res.data.likes;
        setResponse(responseNum);
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, putFn };
};

// プランの添付ファイルをダウンロード
export const useGetFiles = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<Attachment | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async (uuid: string) => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans/attachments/${uuid}`;
    await axios
      .put<Attachment>(url, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseData = await res.data;
        setResponse(responseData);
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, getFn };
};

// いいねした旅行プランの一覧取得
export const useGetLikePlans = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<FormatTripPlans | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans/likes/me`;
    await axios
      .get<TripPlanResponses>(url, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseData = await res.data;
        const planData: FormatTripPlan[] = await Promise.all(
          responseData.tripPlans.map(async (tripPlan) => {
            const imgUrl = await getImgSrc(tripPlan.attachment.uuid);
            const price = sumPrice(
              tripPlan.items.map((v) => {
                return v.price;
              })
            );
            return {
              ...tripPlan,
              imgUrl,
              price,
            };
          })
        );
        setResponse({ tripPlans: planData });
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, []);
  return { loading, error, response, getFn };
};
