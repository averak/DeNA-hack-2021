import type { VFC } from "react";
import { LoginAuthField } from "src/components/User/AuthField";

import type { LoginParam } from "../../utils/hooks/userApi";
import { useLogin as UseLogin } from "../../utils/hooks/userApi";

const LoginPage: VFC = () => {
  // const { getFn, response } = UseLogin();
  const { getFn } = UseLogin();

  const authLogin = (mail: string, password: string) => {
    const param: LoginParam = {
      email: mail,
      password: password,
    };
    getFn(param).then(() => {
      // console.log(response)
    });
    return;
  };
  return (
    <div className="flex relative justify-center items-center w-full h-screen bg-blue-500">
      <div className="w-full h-auto max-w-[320px]">
        <LoginAuthField submitText="ログイン" handleSubmit={authLogin} />
      </div>
    </div>
  );
};

export default LoginPage;
