import type { VFC } from "react";
import { Layout } from "src/components/Layout";

const Error: VFC = () => {
  return (
    <Layout title="エラー" pathList={[]}>
      <div>error</div>;
    </Layout>
  );
};

export default Error;
