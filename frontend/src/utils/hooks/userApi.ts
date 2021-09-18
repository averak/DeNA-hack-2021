import axios from "axios";
import { useCallback, useState } from "react";

import { cookieSet, getTokenHeader } from "../libs/accessToken";

const hostname = process.env.NEXT_PUBLIC_BASE_URI;

export type SignUpParam = {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
};

export type LoginParam = {
  email: string;
  password: string;
};

export type PasswordParam = {
  currentPassword: string;
  newPassword: string;
};

export type UserParam = {
  email: string;
  firstName: string;
  lastName: string;
};

export type UserResponse = {
  accessToken: string;
  tokenType: string;
};

export type ProfileResponse = {
  email: string;
  firstName: string;
  id: number;
  lastName: string;
};

// サインアップ(ユーザ登録)
export const useSignUp = () => {
  const url = `${hostname}/api/signup`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<UserResponse | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const postFn = useCallback(
    async (params: SignUpParam) => {
      setLoading(true);
      await axios
        .post<UserResponse>(url, params, {
          headers: { Authorization: getTokenHeader() },
        })
        .then(async (res) => {
          const responseData = await res.data;
          cookieSet("tokenType", responseData.tokenType);
          cookieSet("accessToken", responseData.accessToken);
          setResponse(responseData);
        })
        .catch((err) => {
          console.error(err);
          setError(err);
        })
        .finally(() => {
          setLoading(false);
        });
    },
    [url]
  );
  return { loading, error, response, postFn };
};

// ログイン
export const useLogin = () => {
  const url = `${hostname}/api/login`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<UserResponse | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const postFn = useCallback(
    async (params: LoginParam) => {
      setLoading(true);
      await axios
        .post<UserResponse>(url, params, {
          headers: { Authorization: getTokenHeader() },
        })
        .then(async (res) => {
          const responseData = await res.data;
          cookieSet("tokenType", responseData.tokenType);
          cookieSet("accessToken", responseData.accessToken);
          setResponse(responseData);
        })
        .catch((err) => {
          console.error(err);
          setError(err);
        })
        .finally(() => {
          setLoading(false);
        });
    },
    [url]
  );
  return { loading, error, response, postFn };
};

// ユーザ情報の取得
export const useGetUserProfile = () => {
  const url = `${hostname}/api/users/me`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<ProfileResponse | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .get<ProfileResponse>(url, {
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
  }, [url]);
  return { loading, error, response, getFn };
};

// ユーザ情報の更新
export const usePutUserProfile = () => {
  const url = `${hostname}/api/users/me`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const putFn = useCallback(
    async (params: UserParam) => {
      setLoading(true);
      await axios
        .put(url, params, {
          headers: { Authorization: getTokenHeader() },
        })
        .then(async (res) => {
          const responseStatusCode = await res.status;
          if (responseStatusCode === 200) setResponse("success");
        })
        .catch((err) => {
          console.error(err);
          setError(err);
        })
        .finally(() => {
          setLoading(false);
        });
    },
    [url]
  );
  return { loading, error, response, putFn };
};

// ユーザ削除
export const useDeleteUser = () => {
  const url = `${hostname}/api/users/me`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const deleteFn = useCallback(async () => {
    setLoading(true);
    await axios
      .delete(url, {
        headers: { Authorization: getTokenHeader() },
      })
      .then(async (res) => {
        const responseStatusCode = await res.status;
        if (responseStatusCode === 200) setResponse("success");
      })
      .catch((err) => {
        console.error(err);
        setError(err);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [url]);
  return { loading, error, response, deleteFn };
};

// パスワードの更新
export const usePutPassword = () => {
  const url = `${hostname}/api/users/me/password`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const putFn = useCallback(
    async (params: PasswordParam) => {
      setLoading(true);
      await axios
        .put(url, params, {
          headers: { Authorization: getTokenHeader() },
        })
        .then(async (res) => {
          const statusCode = await res.status;
          if (statusCode === 200) {
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
    [url]
  );
  return { loading, error, response, putFn };
};
