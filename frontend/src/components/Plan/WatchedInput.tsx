import type { ChangeEvent, VFC } from "react";
import { useState } from "react";

type InputProps = {
  onClick: (value: string) => void;
};

export const WatchedInput: VFC<InputProps> = ({ onClick }) => {
  const [value, setValue] = useState<string>("");
  const [validate, setValidate] = useState<boolean>(false);
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    if (!event.currentTarget?.value) setValue("");
    if (event.currentTarget?.value.length >= 30) setValidate(true);

    setValue(event.currentTarget.value);
  };
  return (
    <div>
      <div className="flex gap-2 pl-3 h-8">
        <input
          className="pl-2 w-full font-normal rounded border border-gray-300 border-solid"
          onChange={handleChange}
          value={value}
        />
        <button
          disabled={validate}
          className="w-16 font-bold text-gray-700 bg-yellow-c2 rounded"
          onClick={() => {
            onClick(value);
            setValue("");
            return;
          }}
        >
          追加
        </button>
      </div>
      {validate && (
        <div className="py-1 text-xs text-red-500">
          30字以内で入力してください
        </div>
      )}
    </div>
  );
};
