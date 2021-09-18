import { PlusIcon, XCircleIcon } from "@heroicons/react/solid";
import type { VFC } from "react";
import { useCallback, useState } from "react";
import type { FieldValues, UseFormRegister } from "react-hook-form";

const inputStyle =
  "font-normal rounded border border-solid border-gray-300 text-base w-full bg-blue-c3";

const dummyDate = "2021-09-18T14:06:54.940Z";

type StepFormProps = {
  register: UseFormRegister<FieldValues>;
  errors: any;
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
        const error = errors ? errors[i] : null;
        return (
          <div
            className="relative py-9 border-t border-gray-300 border-solid"
            key={i}
          >
            <button
              onClick={deleteItem}
              className="absolute top-5 right-0 w-7 h-7"
            >
              <XCircleIcon className="text-blue-c2" />
            </button>
            <p className="pb-2 text-left">目的地名</p>
            <input
              {...register(`items.${i}.title`, { required: true })}
              className={`h-9 ${inputStyle}`}
            />
            {error?.title.type == "required" && (
              <div className="py-1 text-xs text-red-500">
                目的地名は必須です
              </div>
            )}

            <p className="pt-6 text-left">説明文</p>
            <textarea
              {...register(`items.${i}.description`, { required: true })}
              className={`min-h-[90px] ${inputStyle}`}
            />
            {error?.description.type == "required" && (
              <div className="py-1 text-xs text-red-500">説明文は必須です</div>
            )}

            <p className="pt-6 text-left">想定金額</p>
            <div className="flex gap-3 justify-start items-end h-9">
              <input
                type="number"
                {...register(`items.${i}.price`, {
                  required: true,
                  valueAsNumber: true,
                })}
                className={`h-9 ${inputStyle}`}
              />
              <p>円</p>
            </div>
            {error?.price.type == "required" && (
              <div className="py-1 text-xs text-red-500">
                想定金額は必須です
              </div>
            )}
            <input
              {...register(`items.${i}.finishAt`, { required: true })}
              type="hidden"
              value={dummyDate}
            />
            <input
              {...register(`items.${i}.startAt`, { required: true })}
              type="hidden"
              value={dummyDate}
            />
            <input
              {...register(`items.${i}.itemOrder`, { required: true })}
              type="hidden"
              value={i}
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
