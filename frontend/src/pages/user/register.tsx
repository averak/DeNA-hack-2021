import type { VFC } from "react";
import { RegistAuthField } from "src/components/User/AuthField";

import type { SignUpParam } from "../../utils/hooks/userApi";
import { useSignUp as UseSignUp } from "../../utils/hooks/userApi";

const UserRegisterPage: VFC = () => {
  // const { getFn, response } = UseSignUp();
  const { getFn } = UseSignUp();

  const authResiter = (
    firstName: string,
    lastName: string,
    mail: string,
    password: string
  ) => {
    const param: SignUpParam = {
      firstName: firstName,
      lastName: lastName,
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
        <RegistAuthField submitText="新規登録" handleSubmit={authResiter} />
      </div>
    </div>
  );
};

export default UserRegisterPage;
