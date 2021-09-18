import { PlusIcon } from "@heroicons/react/solid";
import type { VFC } from "react";
import { useCallback, useState } from "react";

import { StepForm } from "./StepForm";

export const StepFormWrapper: VFC = () => {
  const [items, setItems] = useState<number>(0);

  const addItems = useCallback(() => {
    setItems(items + 1);
  }, [items]);

  const deleteItem = useCallback(() => {
    setItems(items - 1);
  }, [items]);
  return (
    <div>
      {[...Array(items + 1).keys()].map((i) => {
        return <StepForm key={i} onClick={deleteItem} />;
      })}

      <button
        className="flex gap-3 justify-center items-center w-full h-10 text-base text-gray-600 rounded border border-gray-400 border-dashed"
        onClick={addItems}
      >
        目的地を追加
        <PlusIcon className="w-5 h-5" />
      </button>
    </div>
  );
};
