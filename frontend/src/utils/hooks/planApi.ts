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
  items: TripPlanItem[];
  likes: number;
  regionId: number;
  tags: string[];
  title: string;
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
  const [response, setResponse] = useState<TripPlanResponses | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans`;
    await axios
      .get<TripPlanResponses>(url, {
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

// プランにいいねを押す
export const usePutLikes = () => {
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<number | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const putFn = useCallback(async (tripPlanId: number, params: LikesParam) => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans/${tripPlanId}/likes`;
    await axios
      .put<{ num: number }>(url, params, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseNum = await res.data.num;
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
  const [response, setResponse] = useState<TripPlanResponses | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    const url = `${hostname}/api/trip_plans/likes/me`;
    await axios
      .put<TripPlanResponses>(url, {
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
