import axios from "axios";
import { useCallback, useState } from "react";

const hostname = process.env.NEXT_PUBLIC_BASE_URI;

const tokenType = localStorage.getItem("tokenType");
const accessToken = localStorage.getItem("accessToken");
axios.defaults.headers.common["Autorization"] = `${tokenType} ${accessToken}`;

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
export const useSignUp = (params: SignUpParam) => {
  const url = `${hostname}/api/signup`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<UserResponse | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .post<UserResponse>(url, params)
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
  }, [params, url]);
  return { loading, error, response, getFn };
};

// ログイン
export const useLogin = (params: LoginParam) => {
  const url = `${hostname}/api/login`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<UserResponse | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .post<UserResponse>(url, params)
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
  }, [params, url]);
  return { loading, error, response, getFn };
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
      .post<ProfileResponse>(url)
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

// パスワードの更新
export const usePutPassword = (params: PasswordParam) => {
  const url = `${hostname}/api/users/me/password`;
  const [loading, setLoading] = useState<boolean>(false);
  const [response, setResponse] = useState<string | null>(null);
  const [error, setError] = useState<Error | null>(null);

  const getFn = useCallback(async () => {
    setLoading(true);
    await axios
      .put(url, params)
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
  }, [params, url]);
  return { loading, error, response, getFn };
};
