import type { ReactNode } from "react";

import { Header } from "./Header";

type LayoutProps = {
  children: ReactNode;
  title: string;
  pathList: PathData[];
};

export type PathData = {
  pathTitle: string;
  pathLink: string;
};

export const Layout = ({ children, ...props }: LayoutProps) => {
  return (
    <div>
      <Header title={props.title} pathList={props.pathList} />
      <div className="pt-20">{children}</div>
    </div>
  );
};
