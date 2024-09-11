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

function TotalUser() {
  const navigate = useNavigate();
  const [user, setUser] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);
  const [userData, setUserData] = useState([]);
  const defaultImage =
    "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKJQp8ndvEkIa-u1rMgJxVc7BBsR11uSLHGA&s";
  const department = "OFL";
  const language = "English";

  useEffect(() => {
    fetchUser();
  }, []);

  const columns = (handleUserDelete, handleClick, handleEdit) => [
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
      render: (_, user) => (
        <Space size="middle">
          <Button
            icon={<EditOutlined />}
            className="bg-green-600 hover:bg-green-700 text-white border-none rounded-md p-2 shadow-md"
            // eslint-disable-next-line no-sequences
            onClick={() => (setIsModalVisible(true), setUserData(user))} // Pass the entire user object
          />
          <Button
            icon={<EyeOutlined />}
            className="bg-white hover:bg-yellow-500 text-yellow-500 border-none rounded-full p-2 shadow-md"
            onClick={() => (handleClick(user), fetchUserById(user.id))}
          />
          <Button
            icon={<DeleteOutlined />}
            onClick={() => handleDelete(user.id)}
            className="bg-white hover:bg-red-700 text-red-600 border-none rounded-full p-2 shadow-md transition-colors duration-300 ease-in-out"
          />
        </Space>
      ),
    },
  ];

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
      console.log(result);
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
    setUserData([]);
  };

  const [profile, setProfile] = useState({
    name: "",
    username: "",
    department: "OFL",
    language: "English",
    profileImage:
      "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKJQp8ndvEkIa-u1rMgJxVc7BBsR11uSLHGA&s",
  });

  const fetchUserById = async (userId) => {
    try {
      const response = await fetch(
        `http://localhost:6060/auth/users/${userId}`
      );
      console.log(response);
      if (response.ok) {
        const userData = await response.json();
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

  const token = localStorage.getItem("token");

  const submitForm = async (e) => {
    e.preventDefault();
    setErrorMessage("");

    const { name, username, password, role } = registerObj;

    setLoading(true);
    try {
      const response = await fetch("http://localhost:6060/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify({
          name,
          username,
          password,
          role,
        }),
      });
      console.log(response);
      const responseData = await response.json();
      if (response.ok) {
        setRegisterObj({ name: "", username: "", password: "", role: "" });
        fetchUser();
        setIsModalVisible(false);
      } else {
        setErrorMessage(responseData.message || "Registration failed");
      }
    } catch (error) {
      console.error("Error:", error);
      setErrorMessage("An error occurred");
    } finally {
      setLoading(false);
    }
  };
  const Editform = async (e) => {
    e.preventDefault();
    setErrorMessage("");
  
    // Only include fields that have changed
    const { name, username, password, role } = registerObj;
  
    // Create an object with only the fields that are not empty and different from userData
    const updatedFields = {};
  
    if (name && name !== userData.name) updatedFields.name = name;
    if (username && username !== userData.username) updatedFields.username = username;
    if (password && password !== userData.password) updatedFields.password = password;
    if (role && role !== userData.role) updatedFields.role = role;
  
    setLoading(true);
    try {
      const response = await fetch(
        `http://localhost:6060/auth/users/${userData.id}`,
        {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${token}`,
          },
          body: JSON.stringify(updatedFields), // Only send the updated fields
        }
      );
  
      const responseData = await response.json();
      if (response.ok) {
        setRegisterObj({ name: "", username: "", password: "", role: "" });
        setUserData([]);
        setIsModalVisible(false);
        fetchUser();
      } else {
        setErrorMessage(responseData.message || "Update failed");
      }
    } catch (error) {
      console.error("Error:", error);
      setErrorMessage("An error occurred");
    } finally {
      setLoading(false);
    }
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
    setSelectedUser(userId);
    setIsModalVisible(true);
  };

  const handleDelete = (userId) => {
    Modal.confirm({
      title: "Are you sure you want to delete this user?",
      content: "This action cannot be undone.",
      okText: "Yes",
      okType: "danger",
      cancelText: "No",
      onOk: () => {
        // Call your delete function here
        handleUserDelete(userId);
      },
      onCancel() {
        // Handle cancel action if needed
      },
    });
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
            <div className="flex flex-col items-center mb-8">
              <div className="relative group">
                <img
                  src={profile.profileImage}
                  alt="Profile"
                  className="w-32 h-32 rounded-full border-4 border-gray-200 shadow-lg transform transition-transform duration-300 ease-in-out hover:scale-105"
                  onError={handleImageError}
                />
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
              <h2 className="text-xl mt-6 font-bold text-gray-700">
                {profile.name}
              </h2>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8 ml-7">
              <div className="flex flex-col items-start space-y-2">
                <div className="flex w-full items-center">
                  <label className="text-sm font-medium text-gray-700 mr-4">
                    Name:
                  </label>
                  <h1 className="text-sm font-semibold text-gray-500">
                    {profile.name}
                  </h1>
                </div>
              </div>

              <div className="flex flex-col items-start space-y-2">
                <div className="flex w-full items-center">
                  <label className="text-sm font-medium text-gray-700 mr-4">
                    Username:
                  </label>
                  <h2 className="text-sm font-semibold text-gray-500">
                    {profile.username}
                  </h2>
                </div>
              </div>

              <div className="flex flex-col items-start space-y-2">
                <div className="flex w-full items-center">
                  <label className="text-sm font-medium text-gray-700 mr-4">
                    Department:
                  </label>
                  <h2 className="text-sm font-semibold text-gray-500">
                    {profile.department}
                  </h2>
                </div>
              </div>

              <div className="flex flex-col items-start space-y-2">
                <div className="flex w-full items-center">
                  <label className="text-sm font-medium text-gray-700 mr-4">
                    Language:
                  </label>
                  <h2 className="text-sm font-semibold text-gray-500">
                    {profile.language}
                  </h2>
                </div>
              </div>
            </div>

            {/* Divider */}
            <div className="border-t border-gray-300 mt-8 mb-8"></div>
          </TitleCard>
        ) : (
          <TitleCard title={"Update Auditor"} topMargin="mt-4">
            {userData.length !== 0 ? (
              <div className="p-4">
                <form onSubmit={Editform} className="space-y-4">
                  <InputText
                    type="text"
                    updateType="name"
                    defaultValue={userData.length !== 0 ? userData.name : ""}
                    labelTitle="Name"
                    updateFormValue={updateFormValue}
                  />
                  <InputText
                    type="text"
                    updateType="username"
                    defaultValue={userData.username ?? ""}
                    labelTitle="Username"
                    updateFormValue={updateFormValue}
                  />
                  <InputText
                    type="password"
                    updateType="password"
                    defaultValue={userData.password ?? ""}
                    labelTitle="Password"
                    updateFormValue={updateFormValue}
                  />
                  <InputText
                    updateType="role"
                    defaultValue={userData.role ?? ""}
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
                    Update
                  </button>
                </form>
              </div>
            ) : (
              <form onSubmit={submitForm} className="space-y-4">
                <InputText
                  type="text"
                  updateType="name"
                  labelTitle="Name"
                  updateFormValue={updateFormValue}
                />
                <InputText
                  type="text"
                  updateType="username"
                  labelTitle="Username"
                  updateFormValue={updateFormValue}
                />
                <InputText
                  type="password"
                  updateType="password"
                  labelTitle="Password"
                  updateFormValue={updateFormValue}
                />
                <InputText
                  updateType="role"
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
            )}
          </TitleCard>
        )}
      </Modal>
    </>
  );
}

export default TotalUser;
