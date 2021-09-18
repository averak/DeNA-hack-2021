import type { VFC } from "react";
import type { PathData } from "src/components/Layout";
import { Layout } from "src/components/Layout";

const MyPage: VFC = () => {
  const pathList: PathData[] = [
    { pathTitle: "マイページ", pathLink: "/user/mypage" },
  ];
  return (
    <Layout title="マイページ" pathList={pathList}>
      <main>MyPage</main>
    </Layout>
  );
};

export default MyPage;
