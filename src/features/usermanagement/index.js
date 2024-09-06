import React, { useState, useEffect } from "react";
import { Table, Button, Modal, Space } from 'antd';
import { PlusOutlined, EyeOutlined, EditOutlined, DeleteOutlined } from "@ant-design/icons";
import { useNavigate } from 'react-router-dom';
import Register from '../user/Register';
import { showNotification } from "../common/headerSlice";
import ProfileSettings from "../settings/profilesettings";

const token = localStorage.getItem("token");

const columns = (handleUserDelete, handleClick) => [
  {
    title: "No",
    dataIndex: "key",
    render: (_, __, index) => index + 1,
  },
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
    render: (text) => <a>{text || 'N/A'}</a>,
  },
  {
    title: 'Username',
    dataIndex: 'username',
    key: 'username',
  },
  {
    title: 'Status',
    key: 'status',
    dataIndex: 'enabled',
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
  }
];

function TotalUser() {
  const navigate = useNavigate();
  const [user, setUser] = useState([]);
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [selectedUser, setSelectedUser] = useState(null);

  useEffect(() => {
    fetchUser();
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
        const filteredUsers = result.filter(user => user.role !== 'ADMIN');
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

      // Update user state after successful deletion
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

  const handleCreateClick = () => {
    navigate('/register'); 
  };

  const handleClick = (userId) => {
    setSelectedUser(userId); // Set selected user
    setIsModalVisible(true); // Show ProfileSystems modal
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

      <div className='mt-4'>
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
          <ProfileSettings user={selectedUser} /> // Render ProfileSystems component with selected user data
        ) : (
          <Register /> // Fallback to Register if no user is selected
        )}
      </Modal>
    </>
  );
}

export default TotalUser;
