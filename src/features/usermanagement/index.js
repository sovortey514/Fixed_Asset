import React, { useState, useEffect } from "react";
import { Table, Button, Modal, Space } from "antd";
import {
  PlusOutlined,
  EyeOutlined,
  EditOutlined,
  DeleteOutlined,
} from "@ant-design/icons";
import { useNavigate } from "react-router-dom";
import Register from "../user/Register";
import { showNotification } from "../common/headerSlice";
import ProfileSettings from "../settings/profilesettings";
import TitleCard from "../../components/Cards/TitleCard";
import InputText from "../../components/Input/InputText";
import ErrorText from "../../components/Typography/ErrorText";

const token = localStorage.getItem("token");

const columns = (handleUserDelete, handleClick) => [
  {
    title: "No",
    dataIndex: "key",
    render: (_, __, index) => index + 1,
  },
  {
    title: "Name",
    dataIndex: "name",
    key: "name",
    render: (text) => <a>{text || "N/A"}</a>,
  },
  {
    title: "Username",
    dataIndex: "username",
    key: "username",
  },
  {
    title: "Status",
    key: "status",
    dataIndex: "enabled",
    render: (enabled) => (
      <>
        {enabled ? (
          <span className="px-2 py-1 rounded-md bg-green-200 text-green-800">
            Active
          </span>
        ) : (
          <span className="px-2 py-1 rounded-md bg-red-100 text-red-800">
            Inactive
          </span>
        )}
      </>
    ),
  },
  {
    title: "Action",
    key: "action",
    render: (_, userId) => (
      <Space size="middle">
        <Button
          icon={<EditOutlined />}
          className="bg-green-600 hover:bg-green-700 text-white border-none rounded-md p-2 shadow-md"
          // onClick={() => handleEdit(record)} // Add your edit logic here
        />
        <Button
          icon={<EyeOutlined />}
          className="bg-white hover:bg-yellow-500 text-yellow-500 border-none rounded-full p-2 shadow-md"
          onClick={() => handleClick(userId)}
        />
        <Button
          icon={<DeleteOutlined />}
          onClick={() => handleUserDelete(userId.id)}
          className="bg-white hover:bg-red-700 text-red-600 border-none rounded-full p-2 shadow-md transition-colors duration-300 ease-in-out"
        />
      </Space>
    ),
  },
];

function TotalUser() {
  const navigate = useNavigate();
  const [user, setUser] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  useEffect(() => {
    fetchUser(1);
  }, []);

  const fetchUser = async () => {
    try {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      const response = await fetch("http://localhost:6060/auth/users", {
        headers,
      });
      const result = await response.json();
      console.log(result);
      if (result) {
        const filteredUsers = result.filter((user) => user.role !== "ADMIN");
        setUser(filteredUsers || []);
      } else {
        showNotification.error({
          message: "Failed to fetch User",
          description: result.error,
        });
      }
    } catch (error) {
      console.error("Error fetching user:", error);
    }
  };

  const handleUserDelete = async (userId) => {
    try {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      await fetch(`http://localhost:6060/auth/users/${userId}`, {
        method: "DELETE",
        headers,
      });

      setUser((prevUser) => prevUser.filter((user) => user.id !== userId));

      showNotification.success({
        message: "User Deleted",
        description: "User has been deleted successfully.",
      });
    } catch (error) {
      console.error("Error deleting User:", error);
      showNotification.error({
        message: "Failed to delete User",
        description: error.message,
      });
    }
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    setSelectedUser(null);
  };

  const [profile, setProfile] = useState({
    name: "",
    username: "",
    department: "OFL",
    language: "English",
    profileImage:
      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKJQp8ndvEkIa-u1rMgJxVc7BBsR11uSLHGA&s",
  });

  const defaultImage =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKJQp8ndvEkIa-u1rMgJxVc7BBsR11uSLHGA&s";

  const department = "OFL";
  const language = "English";

  useEffect(() => {
    fetchUserById();
  });

  const fetchUserById = async (userId) => {
    try {
      const response = await fetch(
        `http://localhost:6060/auth/users/${userId}`
      );
      if (response.ok) {
        const userData = await response.json();
        // Log the fetched data to the console
        console.log("Fetched user data:", userData);

        setProfile({
          name: userData.name || "",
          username: userData.username || "", // Assuming 'username' should be used for email
          profileImage: userData.profileImage || defaultImage,
          department: userData.department || department,
          language: userData.language || language,
        });
      } else {
        console.error("Failed to fetch user:", response.statusText);
      }
    } catch (error) {
      console.error("Error fetching user:", error);
    }
  };

  const INITIAL_REGISTER_OBJ = {
    name: "",
    username: "",
    password: "",
    role: "",
  };

  const [loading, setLoading] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const [registerObj, setRegisterObj] = useState(INITIAL_REGISTER_OBJ);

  // const navigate = useNavigate();
  const token = localStorage.getItem("token");

  const submitForm = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    const { name, username, password, role } = registerObj;

    // if (!name.trim()) {
    //   return setErrorMessage("Name is required!");
    // }
    // if (!email.trim()) {
    //   return setErrorMessage("Email is required!");
    // }
    // if (!emailRegex.test(email.trim())) {
    //   return setErrorMessage("Invalid email format!");
    // }
    // if (!password.trim()) {
    //   return setErrorMessage("Password is required!");
    // }
    // if (password.trim().length < 6) {
    //   return setErrorMessage("Password must be at least 6 characters long!");
    // }
    // if (!role.trim()) {
    //   return setErrorMessage("Role is required!");
    // }
    // if (!validRoles.includes(role.trim())) {
    //   return setErrorMessage("Invalid role selected!");
    // }

    setLoading(true);
    try {
      // const result = await response.json();
      const response = await fetch("http://localhost:6060/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name: registerObj.name,
          username: registerObj.username,
          password: registerObj.password,
          role: registerObj.role,
        }),
      });

      console.log({ response });

      const responseData = await response.json();
      if (response.ok) {
        // if (responseData.statusCode === 200) {
        //   navigate('/login');
        // } else {
        //   setErrorMessage(responseData.message || "Registration failed");
        // }
      } else {
        setErrorMessage(responseData.message || "Registration failed");
      }
      setIsModalVisible(false);
      fetchUser();setRegisterObj({ ...INITIAL_REGISTER_OBJ, updateType: "value" });
    } catch (error) {
      console.error("Error:", error);
      setErrorMessage("An error occurred");
    } finally {
      setLoading(false);
    }
  };

  const handleCreateClick = () => {
    navigate("/register");
  };
  const handleImageError = () => {
    setProfile((prev) => ({ ...prev, profileImage: defaultImage }));
  };
  const updateFormValue = ({ updateType, value }) => {
    setProfile((prev) => ({ ...prev, [updateType.toLowerCase()]: value }));
    setErrorMessage("");
    setRegisterObj({ ...registerObj, [updateType]: value });
    console.log("registerObj ", registerObj);
  };

  const handleClick = (userId) => {
    setSelectedUser(userId); // Set selected user
    setIsModalVisible(true); // Show ProfileSystems modal
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    console.log("onchange", e);
    setRegisterObj((prev) => ({ ...prev, [name]: value }));
  };

  return (
    <>
      <div className="flex justify-between mb-4">
        <Button
          size="middle"
          shape="circle"
          icon={<PlusOutlined />}
          onClick={() => setIsModalVisible(true)}
        />
      </div>

      <div className="mt-4">
        <Table
          columns={columns(handleUserDelete, handleClick)} // Pass handleClick to columns
          dataSource={user.map((u, index) => ({ ...u, key: index }))}
          rowKey="id"
        />
      </div>
      <Modal
        visible={isModalVisible}
        width={500}
        footer={null}
        onCancel={handleCancel}
      >
        {selectedUser ? (
          <TitleCard topMargin="mt-4">
            {/* Profile Image Section */}
            <div className="flex flex-col items-center mb-8">
              <div className="relative group">
                <img
                  src={profile.profileImage}
                  alt="Profile"
                  className="w-32 h-32 rounded-full border-4 border-gray-200 shadow-lg transform transition-transform duration-300 ease-in-out hover:scale-105"
                  onError={handleImageError}
                />
                {/* Edit Button */}
                <button
                  className="absolute bottom-0 right-0 bg-white text-yellow-500 p-2 rounded-full shadow-lg transition-colors duration-300 ease-in-out hover:text-yellow-600"
                  aria-label="Edit Profile Picture"
                >
                  <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-6 w-6"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth="2"
                      d="M12 4v16m8-8H4"
                    />
                  </svg>
                </button>
              </div>
              <h2 className="text-2xl mt-4 font-semibold text-gray-800">
                {selectedUser.name}
              </h2>
            </div>

            {/* Form Fields */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
              <InputText
                labelTitle="Name"
                defaultValue={selectedUser.name}
                updateFormValue={updateFormValue}
              />
              <InputText
                labelTitle="User name"
                defaultValue={profile.username}
                updateFormValue={updateFormValue}
              />
              <InputText
                labelTitle="Department"
                defaultValue={profile.department}
                updateFormValue={updateFormValue}
              />
              <InputText
                labelTitle="Language"
                defaultValue={profile.language}
                updateFormValue={updateFormValue}
              />
            </div>

            {/* Divider */}
            <div className="border-t border-gray-300 mt-6 mb-8"></div>
          </TitleCard> // Render ProfileSystems component with selected user data
        ) : (
          <TitleCard title={"Create"} topMargin="mt-4">
            <div className="p-4">
              <form onSubmit={submitForm} className="space-y-4">
                <InputText
                  type="text"
                  updateType="name"
                  defaultValue={registerObj.name}
                  labelTitle="Name"
                  updateFormValue={updateFormValue}
                />
                <InputText
                  type="text"
                  updateType="username"
                  defaultValue={registerObj.username}
                  labelTitle="Username"
                  updateFormValue={updateFormValue}
                />
                <InputText
                  type="password"
                  updateType="password"
                  defaultValue={registerObj.password}
                  labelTitle="Password"
                  updateFormValue={updateFormValue}
                />
                <InputText
                  updateType="role"
                  defaultValue={registerObj.role}
                  labelTitle="Role"
                  updateFormValue={updateFormValue}
                />
                <ErrorText>{errorMessage}</ErrorText>
                <button
                  type="submit"
                  className={`btn w-full bg-yellow-500 hover:bg-yellow-200 text-white hover:text-black ${
                    loading ? "loading" : ""
                  }`}
                >
                  Create
                </button>
              </form>
            </div>
          </TitleCard> // Fallback to Register if no user is selected
        )}
      </Modal>
    </>
  );
}

export default TotalUser;
