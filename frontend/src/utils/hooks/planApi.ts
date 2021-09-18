import axios from "axios";
import { useCallback, useState } from "react";

const hostname = process.env.NEXT_PUBLIC_BASE_URI;

export type TripPlanParam = {
  title: string;
  description: string;
  regionId: number;
  tags: string[];
  attachment: Blob;
  items: TripPlanItem;
};

export type LikesParam = {
  isLike: boolean;
};

export type TripPlanResponse = {
  id: number;
  title: string;
  description: string;
  region_id: number;
  user_id: number;
  created_at: Date;
  updated_at: Date;
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
  id: number;
  tripPlanId: number;
  itemOrder: number;
  title: string;
  description: string;
  price: number;
  startAt: Date;
  finishAt: Date;
};

// 全てのプランの取得
export const useGetAllPlan = () => {
  const url = `${hostname}/api/trip_plans`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<TripPlanResponse[] | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .get<TripPlanResponse[]>(url)
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
  }, [url]);
  return { loading, error, response, getFn };
};

// プラン詳細を取得
export const useGetPlanDetail = (tripPlanId: number) => {
  const url = `${hostname}/api/trip_plans/${tripPlanId}`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<TripDetailResponse | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .get<TripDetailResponse>(url)
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
  }, [url]);
  return { loading, error, response, getFn };
};

// プラン作成
export const usePostPlan = (params: TripPlanParam) => {
  const url = `${hostname}/api/trip_plans`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .post<TripDetailResponse>(url, params)
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
  }, [params, url]);
  return { loading, error, response, getFn };
};

// プラン更新
export const usePutPlan = (params: TripPlanParam) => {
  const url = `${hostname}/api/trip_plans`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .put(url, params)
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
  }, [params, url]);
  return { loading, error, response, getFn };
};

// プラン削除
export const useDeletePlan = (tripPlanId: number) => {
  const url = `${hostname}/api/trip_plans/${tripPlanId}`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .delete(url)
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
  }, [url]);
  return { loading, error, response, getFn };
};

// プランにいいねを押す
export const usePutLikes = (tripPlanId: number, params: LikesParam) => {
  const url = `${hostname}/api/trip_plans/${tripPlanId}/likes`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .put(url, params)
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
  }, [params, url]);
  return { loading, error, response, getFn };
};
