import type { NextPageContext } from "next";
import { parseCookies, setCookie } from "nookies";

import type { UserResponse } from "../hooks/userApi";

// cookie取得
const getCookie = (ctx?: NextPageContext) => {
  const cookie = parseCookies(ctx);
  return cookie;
};

const getToken = (): UserResponse => {
  const tokenType = getCookie().tokenType;
  const accessToken = getCookie().accessToken;
  return {
    tokenType: tokenType,
    accessToken: accessToken,
  };
};

export const getTokenHeader = (): string => {
  const tokenData = getToken();
  return `${tokenData.tokenType} ${tokenData.accessToken}`;
};

export const cookieSet = (key: string, value: string) => {
  setCookie(null, key, value, {
    maxAge: 24 * 60 * 60,
  });
};
