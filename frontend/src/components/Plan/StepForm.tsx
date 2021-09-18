import { XIcon } from "@heroicons/react/solid";
import type { VFC } from "react";

const inputStyle =
  "font-normal rounded border border-solid border-gray-300 w-full";

type StepFormProps = {
  onClick: () => void;
};

export const StepForm: VFC<StepFormProps> = ({ onClick }) => {
  return (
    <div className="relative py-6">
      <button onClick={onClick} className="absolute top-0 right-0 w-4 h-4">
        {" "}
        <XIcon />
      </button>
      <p className="pb-2 text-left">目的地名</p>
      <input className={`h-9 ${inputStyle}`} />

      <p className="pt-6 text-left">説明文</p>
      <textarea className={`min-h-[90px] ${inputStyle}`} />

      <p className="pt-6 text-left">想定金額</p>
      <div className="flex gap-3 justify-start items-end h-9">
        <input className={`h-9 ${inputStyle}`} />
        <p>円</p>
      </div>
    </div>
  );
};
