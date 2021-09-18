import type { VFC } from "react";
import type { PathData } from "src/components/Layout";
import { Layout } from "src/components/Layout";

const SchedulePage: VFC = () => {
  // プランidを取得する処理が必要
  const planId = 0;

  // スケジュールidを取得する処理が必要
  const scheduleId = 0;

  const pathList: PathData[] = [
    {
      pathTitle: "プラン詳細画面",
      pathLink: `/plan/${planId}`,
    },
    {
      pathTitle: "日程詳細画面",
      pathLink: `/schedule/${scheduleId}`,
    },
  ];
  return (
    <Layout title="日程詳細" pathList={pathList}>
      <main>SchedulePage</main>
    </Layout>
  );
};

export default SchedulePage;
