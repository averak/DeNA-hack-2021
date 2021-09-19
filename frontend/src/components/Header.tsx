import {
  ChevronRightIcon,
  HomeIcon,
  LoginIcon,
  UserIcon,
} from "@heroicons/react/solid";
import Link from "next/link";
import type { VFC } from "react";
import { useEffect, useState } from "react";

import { isAccessToken } from "../utils/libs/accessToken";
import type { PathData } from "./Layout";

type HeaderProps = {
  title: string;
  pathList: PathData[];
};

export const Header: VFC<HeaderProps> = (props) => {
  const [url, setUrl] = useState<string>("/user/login");
  const [loaded, setLoaded] = useState<boolean>(true);

  const isLogin = isAccessToken();

  useEffect(() => {
    isLogin ? setUrl("/user/mypage") : setUrl("/user/login");
    setLoaded(false);
  }, []);

  const ShowUser = () => {
    return isLogin ? (
      <UserIcon className="w-8 h-8" />
    ) : (
      <LoginIcon className="w-8 h-8" />
    );
  };

  return (
    <header className="fixed z-50 w-screen">
      <div className="flex relative justify-between p-2 w-screen h-12 text-white align-middle bg-gradient-to-r from-blue-c2 to-blue-c1">
        <div className="flex">
          <Link href="/">
            <a className="block align-middle">
              <img src="/white_logo_title.png" alt="logo" className="h-8"></img>
            </a>
          </Link>
        </div>
        <p className="absolute top-1/2 left-1/2 text-xl font-bold transform -translate-x-1/2 -translate-y-1/2">
          {props.title}
        </p>
        <Link href={url}>
          <a>{!loaded && <ShowUser />}</a>
        </Link>
      </div>

      <div className="flex justify-start p-2 w-screen h-10 text-gray-700 bg-gray-100">
        <Link href="/">
          <a>
            <HomeIcon className="w-6 h-6" />
          </a>
        </Link>
        {props.pathList.map((path, i) => {
          return (
            <div key={i} className="flex justify-start">
              <ChevronRightIcon className="mx-1 mt-1 w-5 h-5" />
              <Link href={path.pathLink}>
                <a className="">{path.pathTitle}</a>
              </Link>
            </div>
          );
        })}
      </div>
    </header>
  );
};
