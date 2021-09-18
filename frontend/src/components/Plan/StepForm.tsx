import { PlusIcon, XIcon } from "@heroicons/react/solid";
import type { VFC } from "react";
import { useCallback, useState } from "react";
import type {
  FieldErrors,
  FieldValues,
  UseFormRegister,
} from "react-hook-form";

const inputStyle =
  "font-normal rounded border border-solid border-gray-300 w-full";

const dummyDate = "2021-09-18T14:06:54.940Z";

type StepFormProps = {
  register: UseFormRegister<FieldValues>;
  errors: FieldErrors<FieldValues>;
};

export const StepForm: VFC<StepFormProps> = ({ register, errors }) => {
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
        return (
          <div className="relative py-6" key={i}>
            <button
              onClick={deleteItem}
              className="absolute top-0 right-0 w-4 h-4"
            >
              <XIcon />
            </button>
            <p className="pb-2 text-left">目的地名</p>
            <input
              {...register(`items.${i}.title`)}
              className={`h-9 ${inputStyle}`}
            />

            <p className="pt-6 text-left">説明文</p>
            <textarea
              {...register(`items.${i}.description`)}
              className={`min-h-[90px] ${inputStyle}`}
            />

            <p className="pt-6 text-left">想定金額</p>
            <div className="flex gap-3 justify-start items-end h-9">
              <input
                {...register(`items.${i}.price`)}
                className={`h-9 ${inputStyle}`}
              />
              <p>円</p>
            </div>
            <input
              {...register(`items.${i}.finishAt`)}
              type="hidden"
              value={dummyDate}
            />
            <input
              {...register(`items.${i}.startAt`)}
              type="hidden"
              value={dummyDate}
            />
          </div>
        );
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
