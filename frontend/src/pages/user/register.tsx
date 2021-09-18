import type { VFC } from "react";
import { AuthField } from "src/components/User/AuthField";

const UserRegisterPage: VFC = () => {
  const authLogin = () => {
    return;
  };
  return (
    <div className="flex relative justify-center items-center w-full h-screen bg-blue-500">
      <div className="w-full max-w-[320px] h-auto">
        <AuthField submitText="新規登録" handleSubmit={authLogin} />
      </div>
    </div>
  );
};

export default UserRegisterPage;
