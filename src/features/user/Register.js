import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import LandingIntro from "./LandingIntro";
import ErrorText from "../../components/Typography/ErrorText";
import InputText from "../../components/Input/InputText";

function Register() {
  const INITIAL_REGISTER_OBJ = {
    name: "",
    email: "",
    password: "",
    role: "",
  };

  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [registerObj, setRegisterObj] = useState(INITIAL_REGISTER_OBJ);

  const navigate = useNavigate();
  const token = localStorage.getItem('token');

  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  const validRoles = ["Admin", "User", "Moderator"]; // Adjust as needed

  const submitForm = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    const { name, email, password, role } = registerObj;

    if (!name.trim()) {
      return setErrorMessage("Name is required!");
    }
    if (!email.trim()) {
      return setErrorMessage("Email is required!");
    }
    if (!emailRegex.test(email.trim())) {
      return setErrorMessage("Invalid email format!");
    }
    if (!password.trim()) {
      return setErrorMessage("Password is required!");
    }
    if (password.trim().length < 6) {
      return setErrorMessage("Password must be at least 6 characters long!");
    }
    if (!role.trim()) {
      return setErrorMessage("Role is required!");
    }
    if (!validRoles.includes(role.trim())) {
      return setErrorMessage("Invalid role selected!");
    }

    setLoading(true);

    try {
      const headers = { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` };
      const result = await response.json();
      const response = await fetch('http://localhost:6060/auth/signup', {
        method: 'POST',
        headers,
        body: JSON.stringify(registerObj),
      });

      const responseData = await response.json();
      if (response.ok) {
        if (responseData.statusCode === 200) {
          navigate('/login');
        } else {
          setErrorMessage(responseData.message || "Registration failed");
        }
      } else {
        setErrorMessage(responseData.message || "Registration failed");
      }
    } catch (error) {
      console.error('Error:', error);
      setErrorMessage("An error occurred");
    } finally {
      setLoading(false);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setRegisterObj((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <div className="min-h-screen bg-base-200 flex items-center">
      <div className="card mx-auto w-full max-w-5xl shadow-xl">
        <div className="grid md:grid-cols-2 grid-cols-1 bg-base-100 rounded-xl">
          <div>
            <LandingIntro />
          </div>
          <div className="py-24 px-10">
            <h2 className="text-2xl font-semibold mb-2 text-center">Register</h2>
            <form onSubmit={submitForm}>
              <div className="mb-4">
                <InputText
                  name="name"
                  value={registerObj.name}
                  containerStyle="mt-4"
                  labelTitle="Name"
                  updateFormValue={handleInputChange}
                />
                <InputText
                  name="email"
                  value={registerObj.email}
                  containerStyle="mt-4"
                  labelTitle="Email"
                  updateFormValue={handleInputChange}
                />
                <InputText
                  name="password"
                  type="password"
                  value={registerObj.password}
                  containerStyle="mt-4"
                  labelTitle="Password"
                  updateFormValue={handleInputChange}
                />
                <InputText
                  name="role"
                  value={registerObj.role}
                  containerStyle="mt-4"
                  labelTitle="Role"
                  updateFormValue={handleInputChange}
                />
              </div>

              <ErrorText styleClass="mt-8">{errorMessage}</ErrorText>
              <button
                type="submit"
                className={`btn mt-2 w-full bg-yellow-500 hover:bg-yellow-200 text-white hover:text-black ${loading ? "loading" : ""}`}
              >
                Register
              </button>

              <div className="text-center mt-4">
                Already have an account?{" "}
                <Link to="/login">
                  <span className="inline-block hover:text-primary hover:underline hover:cursor-pointer transition duration-200">
                    Login
                  </span>
                </Link>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
}

export default Register;
