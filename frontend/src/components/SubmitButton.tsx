import { PlusIcon } from "@heroicons/react/solid";
import Link from "next/link";
import type { VFC } from "react";

export const SubmitButton: VFC = () => {
  return (
    <Link href="/plan/register">
      <a className="flex fixed right-4 bottom-4 justify-center items-center w-12 h-12 bg-blue-400 rounded-full">
        <PlusIcon className="m-auto w-10 h-10 text-white" />
      </a>
    </Link>
  );
};
