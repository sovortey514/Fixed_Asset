import { useState } from 'react';
import { Link } from 'react-router-dom';
import LandingIntro from './LandingIntro';
import ErrorText from '../../components/Typography/ErrorText';
import InputText from '../../components/Input/InputText';

function Login() {
    const INITIAL_LOGIN_OBJ = {
        emailId: "", 
        password: "" 
    };

    const [loading, setLoading] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");
    const [loginObj, setLoginObj] = useState(INITIAL_LOGIN_OBJ);

    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Basic email format validation

    const submitForm = async (e) => {
        e.preventDefault();
        setErrorMessage(""); // Clear any existing error message

        // Check if email field is empty
        if (loginObj.emailId.trim() === "" || loginObj.password.trim() === "") {
            return setErrorMessage("Email and password are required!");
        }

        // Check if email format is valid
        if (!emailRegex.test(loginObj.emailId.trim())) {
            return setErrorMessage("Invalid email format");
        }

        // Check if password length is sufficient
        if (loginObj.password.trim().length < 6) {
            return setErrorMessage("Password must be at least 6 characters long");
        }

        setLoading(true);
        try {
            const response = await fetch('http://localhost:6060/auth/signin', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email: loginObj.emailId,
                    password: loginObj.password,
                }),
            });

            const data = await response.json();
            console.log(data);
            if (data.statusCode ===200) {
                localStorage.setItem("token", data.token);
                setLoading(false);
                window.location.href = '/app/welcome';
            } else {
                setLoading(false);
                setErrorMessage(data.error || "An error occurred during login.");
            }
        } catch (error) {
            setLoading(false);
            setErrorMessage("An error occurred");
        }
    };

    const updateFormValue = ({ updateType, value }) => {
        setErrorMessage("");
        setLoginObj({ ...loginObj, [updateType]: value });
    };

    return (
        <div className="min-h-screen bg-base-200 flex items-center">
            <div className="card mx-auto w-full max-w-5xl shadow-xl">
                <div className="grid md:grid-cols-2 grid-cols-1 bg-base-100 rounded-xl">
                    <div>
                        <LandingIntro />
                    </div>
                    <div className='py-24 px-10'>
                        <h2 className='text-2xl font-semibold mb-2 text-center'>Login</h2>
                        <form onSubmit={submitForm}>
                            <div className="mb-4">
                                <InputText
                                    type="email"
                                    defaultValue={loginObj.emailId}
                                    updateType="emailId"
                                    containerStyle="mt-4"
                                    labelTitle="Email"
                                    updateFormValue={updateFormValue}
                                />
                                <InputText
                                    defaultValue={loginObj.password}
                                    type="password"
                                    updateType="password"
                                    containerStyle="mt-4"
                                    labelTitle="Password"
                                    updateFormValue={updateFormValue}
                                />
                            </div>
                            <ErrorText styleClass="mt-8">{errorMessage}</ErrorText>
                            <button type="submit" className={"btn mt-2 w-full bg-yellow-500 text-white" + (loading ? " loading" : "")}>Login</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default Login;
