import type { ChangeEvent, VFC } from "react";
import { useState } from "react";

type InputProps = {
  onClick: (value: string) => void;
};

export const WatchedInput: VFC<InputProps> = ({ onClick }) => {
  const [value, setValue] = useState<string>("");
  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    if (!event.currentTarget?.value) return;
    setValue(event.currentTarget.value);
  };
  return (
    <div className="flex gap-2 pl-3 h-8">
      <input
        className="w-full font-normal rounded border border-gray-300 border-solid"
        onChange={handleChange}
        value={value}
      />
      <button
        className="w-14 bg-gradient-to-r from-yellow-c2 to-yellow-c1"
        onClick={() => {
          return onClick(value);
        }}
      >
        追加
      </button>
    </div>
  );
};
