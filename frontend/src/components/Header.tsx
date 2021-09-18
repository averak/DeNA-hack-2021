import { ChevronRightIcon, HomeIcon, UserIcon } from "@heroicons/react/solid";
import Link from "next/link";
import type { VFC } from "react";

import type { PathData } from "./Layout";

type HeaderProps = {
  title: string;
  pathList: PathData[];
};

export const Header: VFC<HeaderProps> = (props) => {
  return (
    <header className="fixed z-50 w-screen">
      <div className="flex justify-between w-screen bg-blue-200">
        <Link href="/">
          <a className="block align-middle">Logo</a>
        </Link>
        <h1 className="block">{props.title}</h1>
        <Link href="/user/mypage">
          <a className="block">
            <UserIcon className="w-10 h-10" />
          </a>
        </Link>
      </div>
      <div className="flex justify-start w-screen bg-white">
        <Link href="/">
          <a className="m-0">
            <HomeIcon className="w-5 h-5" />
          </a>
        </Link>
        {props.pathList.map((path, i) => {
          return (
            <div key={i} className="flex justify-start">
              <ChevronRightIcon className="w-5 h-5" />
              <Link href={path.pathLink}>
                <a>{path.pathTitle}</a>
              </Link>
            </div>
          );
        })}
      </div>
    </header>
  );
};
