import React, { useState, useEffect } from "react";
import {
  Button,
  Modal,
  Form,
  Input,
  Select,
  Dropdown,
  Menu,
  Space,
  Table,
  notification,
  Upload,
} from "antd";
import {
  DownOutlined,
  EditOutlined,
  EyeOutlined,
  DeleteOutlined,
  DownloadOutlined,
  ReloadOutlined,
  UploadOutlined,
} from "@ant-design/icons";

import { DatePicker } from "antd";
import moment from "moment";
import { PlusOutlined } from "@ant-design/icons";
import { SearchOutlined } from "@ant-design/icons";
// import { select } from '@nextui-org/react';

const { Option } = Select;
const { RangePicker } = DatePicker;

const TotalAsset = () => {
  const [size, setSize] = useState("large");
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [isViewModalVisible, setIsViewModalVisible] = useState(false);
  const [modalType, setModalType] = useState(""); // 'fixedasset' or 'category'
  const [editCategory, setEditCategory] = useState(null);
  const [editKey, setEditKey] = useState(null);
  const [form] = Form.useForm();
  const [categories, setCategories] = useState([]);
  const [data, setData] = useState([]);
  const token = localStorage.getItem("token");
  const [visibleAssets, setVisibleAssets] = useState([]);
  const [assetDetails, setViewAsset] = useState(null);
  const [item, setItem] = useState([]);
  const [assignasset, setAssignasset] = useState([]);
  const [assetHolders, setAssetHolder] = useState([]);
  const [assetId, setAssetId] = useState(0);
  const [currentRecord, setCurrentRecord] = useState(null);
  const [selectedStatus, setSelectedStatus] = useState(null);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [loading, setLoading] = useState(false);
  const [images, setImages] = useState(null);

  const [a, setA] = useState(0);
  const [assetById, setAssetById] = useState([]);
  const [file, setFile] = useState(null);

  // Define table columns
  const columns = (handleEdit, handleDelete, handleViewHide) => [
    {
      title: "No",
      dataIndex: "key",
      render: (_, r, index) => index + 1,
    },
    {
      title: "Name",
      dataIndex: "name",
      key: "name",
      render: (text) => <a>{text}</a>,
    },
    {
      title: "Category",
      dataIndex: "categoryId",
      key: "categoryId",
      render: (_, categoryId) => {
        return categoryId.category.name;
      },
    },
    {
      title: "Model",
      dataIndex: "model",
      key: "model",
    },
    {
      title: "Year",
      dataIndex: "year",
      key: "year",
    },
    {
      title: "Serial Number",
      dataIndex: "serialNumber",
      key: "serialNumber",
    },
    {
      title: "Purchase Date",
      dataIndex: "purchaseDate",
      key: "purchaseDate",
      render: (date) => (date ? new Date(date).toLocaleDateString() : "N/A"),
    },
    {
      title: "Price",
      dataIndex: "price",
      key: "price",
      render: (price) => `$${price.toFixed(2)}`,
    },
    {
      title: "Unit",
      dataIndex: "unit",
      key: "unit",
    },
    {
      title: "Quantity",
      dataIndex: "quantity",
      key: "quantity",
    },
    {
      title: "Status",
      dataIndex: "statustext",
      key: "statustext",
      render: (statustext) => {
        const status = statustext || "Available";

        return (
          <span
            className={`px-2 py-1 rounded ${
              statustext === "Avaliable"
                ? "bg-green-200 text-green-800"
                : statustext === "In Use"
                ? "bg-red-200 text-red-800"
                : ""
            }`}
          >
            {statustext}
          </span>
        );
      },
    },

    {
      title: "Action",
      key: "action",
      render: (_, record) => (
        <Space size="middle">
          <Button
            icon={<EditOutlined />}
            onClick={() => (handleEdit(record), setItem(record))}
            className="bg-green-600 hover:bg-green-700 text-white border-none rounded-md p-2 shadow-md"
          />
          <Dropdown
            overlay={
              <Menu>
                <Menu.Item key="view">
                  <Button
                    type="link"
                    onClick={() => handleViewHide(record, "view")}
                  >
                    View
                  </Button>
                </Menu.Item>
                <Menu.Item key="hide">
                  <Button
                    type="link"
                    onClick={() => (
                      handleViewHide(record, "hide"), setItem(record)
                    )}
                  >
                    Remove
                  </Button>
                </Menu.Item>
              </Menu>
            }
          >
            <Button
              icon={<EyeOutlined />}
              className="bg-white hover:bg-yellow-500 text-yellow-500 border-none rounded-full p-2 shadow-md"
            />
          </Dropdown>
          {record.statustext === "In Use" && (
            <Button
              // Uncomment and define handleDownload if needed
              onClick={() => {
                setIsModalVisible(true);
                setModalType("return");
                setAssetId(record.id);
                setA(record.id);
              }}
              className="bg-white hover:bg-yellow-500 text-yellow-500 border-none rounded-full p-1 text-xs shadow-sm"
            >
              Return
            </Button>
          )}
        </Space>
      ),
    },
  ];

  useEffect(() => {
    const data = async () => {
      try {
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };
        const response = await fetch(
          `http://localhost:6060/admin/getFixedAssetById/${a}`,
          {
            method: "GET",
            headers,
          }
        );
        if (response.ok) {
          const assetDetails = await response.json();
          setAssetById(assetDetails.fixedAsset);
        } else {
          const errorData = await response.json();
          notification.error({
            message: "Failed to Fetch Asset Details",
            description:
              errorData.message ||
              "There was an error fetching the asset details.",
          });
        }
      } catch (error) {
        notification.error({
          message: "Failed to Update Asset",
          description: "There was an error updating the asset.",
        });
        console.error("Error updating visibility:", error);
      }
    };
    data();
  }, [a]);

  // Fetch categories and assets on component mount
  useEffect(() => {
    fetchCategories();
    fetchFixedAssets();
    fetchAssetHolder();
  }, []);

  const fetchCategories = async () => {
    try {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      const response = await fetch("http://localhost:6060/admin/categories", {
        headers,
      });
      const result = await response.json();
      if (result.statusCode === 200) {
        setCategories(result.categories || []);
      } else {
        notification.error({
          message: "Failed to fetch categories",
          description: result.error,
        });
      }
    } catch (error) {
      console.error("Error fetching categories:", error);
    }
  };

  const fetchAssetHolder = async () => {
    try {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      const response = await fetch(
        "http://localhost:6060/admin/getallassetholders",
        { headers }
      );
      const result = await response.json();

      console.log(result.assetHolders); // Logs the asset holders to the console

      if (result.statusCode === 200) {
        setAssetHolder(result.assetHolders || []);
      } else {
        notification.error({
          message: "Failed to fetch asset holders",
          description: result.error,
        });
      }
    } catch (error) {
      console.error("Error fetching asset holders:", error);
    }
  };

  const fetchFixedAssets = async () => {
    try {
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      const response = await fetch(
        "http://localhost:6060/admin/getAllFixedAssets",
        { headers }
      );
      const result = await response.json();
      if (result.statusCode === 200) {
        console.log(result.fixedAssets);
        setData(result.fixedAssets || []);
      } else {
        notification.error({
          message: "Failed to fetch fixed assets",
          description: result.error,
        });
      }
    } catch (error) {
      console.error("Error fetching fixed assets:", error);
    }
  };

  const showModal = (type) => {
    setModalType(type);
    setEditCategory(null);
    setEditKey(null);
    setIsModalVisible(true);
    setViewAsset(null);
    setAssignasset(null);
    setCurrentRecord(null);
  };

  const handleOk = async () => {
    try {
      const values = await form.validateFields();
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };

      if (modalType === "category") {
        if (editCategory) {
          // Edit existing category
          await fetch(
            `http://localhost:6060/admin/categories/${editCategory.id}`,
            {
              method: "PUT",
              headers,
              body: JSON.stringify(values),
            }
          );

          setCategories((prevCategories) =>
            prevCategories.map((cat) =>
              cat.id === editCategory.id ? { ...cat, ...values } : cat
            )
          );

          notification.success({
            message: "Category Updated",
            description: `Category "${values.name}" has been updated successfully.`,
            duration: 1,
          });
        } else {
          // Create new category
          const response = await fetch(
            "http://localhost:6060/admin/createCategory",
            {
              method: "POST",
              headers,
              body: JSON.stringify(values),
            }
          );

          const result = await response.json();

          if (response.ok) {
            // Successfully created category
            // setCategories((prevCategories) => [...prevCategories, result.category]);
            if (result.statusCode === 400) {
              notification.error({
                message: "Failed",
                description: `Category "${values.name}" already exists.`,
                duration: 1,
              });

              return;
            }
            notification.success({
              message: "Category Created",
              description: `Category "${values.name}" has been created successfully.`,
              duration: 1,
            });

            fetchCategories();
          } else {
            // Handle error response from server
            notification.error({
              message: "Can not create with the same name category",
              description:
                result.message || "Category with this name already exists.",
              duration: 1,
            });
          }
        }
      } else if (modalType === "fixedasset") {
        try {
          if (editKey !== null) {
            // Edit existing asset
            const updateResponse = await fetch(
              `http://localhost:6060/admin/updateFixedAsset/${editKey.id}`,
              {
                method: "PUT",
                headers,
                body: JSON.stringify({
                  ...values,
                  status: "1",
                  statustext: "Avaliable",
                }),
              }
            );

            if (updateResponse.ok) {
              notification.success({
                message: "Asset Updated",
                description: "Fixed asset has been updated successfully.",
                duration: 1,
              });
              fetchFixedAssets();
            } else {
              const updateResult = await updateResponse.json();
              throw new Error(
                updateResult.message || "Failed to update fixed asset."
              );
            }
          } else {
            // Create new asset
            delete values.image;
            const createResponse = await fetch(
              "http://localhost:6060/admin/createFixedAsset",
              {
                method: "POST",
                headers,
                body: JSON.stringify({
                  ...values,
                  status: "1",
                  statustext: "Avaliable",
                }),
              }
            );

            const createResult = await createResponse.json();
            if (createResponse.ok) {
              if (images) {
                let uploadResult;
                let uploadResponse;
                for (let i = 0; i < images.length; i++) {
                  console.log(images[i]);
                  const formData = new FormData();
                  formData.append("file", images[i]);
                  formData.append("fixedAssetId", createResult.fixedAsset.id);
                  uploadResponse = await fetch(
                    "http://localhost:6060/admin/upload_image",
                    {
                      method: "POST",
                      body: formData,
                      headers: {
                        Authorization: `Bearer ${token}`,
                      },
                    }
                  );
                }
                uploadResult = await uploadResponse.json();

                if (uploadResponse.ok) {
                  notification.success({
                    // message: "Image Uploaded",
                    // description: `Image for Fixed Asset "${values.name}" has been uploaded successfully.`,
                    duration: 1,
                  });
                } else {
                  throw new Error(
                    uploadResult.message || "Failed to upload image."
                  );
                }
              }

              notification.success({
                message: "Fixed Asset Created",
                description: `Fixed Asset "${values.name}" has been created successfully.`,
                duration: 1,
              });

              fetchFixedAssets();
            } else {
              // Handle error response from server
              throw new Error(
                createResult.message || "Failed to create fixed asset."
              );
            }
          }
        } catch (error) {
          notification.error({
            message: "Operation Failed",
            description: error.message,
            duration: 1,
          });
        }
      } else if (modalType === "assign") {
        console.log(values);

        if (values && values.id) {
          const requestBody = {
            ...values,
            status: "1",
            statustext: "In Use",
            id: a,
            categoryId: assetById.category.id,
            model: assetById.model,
            name: assetById.name,
            price: assetById.price,
            purchaseDate: assetById.purchaseDate,
            quantity: assetById.quantity,
            serialNumber: assetById.serialNumber,
            unit: assetById.unit,
            year: assetById.year,
          };
          // Optionally include fixedAsset if needed
          if (values.fixedAsset) {
            requestBody.fixedAsset = { id: values.fixedAsset };
          }
          console.log("editKey:", editKey);
          console.log("editKey.id:", editKey ? editKey.id : "No editKey.id");
          try {
            const response = await fetch(
              `http://localhost:6060/admin/updateFixedAsset/${values.id}`,
              {
                method: "PUT",
                headers,
                body: JSON.stringify(requestBody),
              }
            );

            if (response.ok) {
              fetchFixedAssets();
              notification.success({
                message: "Asset Updated",
                description: "Fixed asset has been updated successfully.",
                duration: 1,
              });
            } else {
              const result = await response.json();
              notification.error({
                message: "Update Failed",
                description:
                  result.message ||
                  "An error occurred while updating the fixed asset.",
                duration: 1,
              });
            }
          } catch (error) {
            console.error("Fetch error:", error);
            notification.error({
              message: "Update Failed",
              description: "An unexpected error occurred.",
              duration: 1,
            });
          }
        } else {
          notification.error({
            message: "Update Failed",
            description: "The asset ID is missing or invalid.",
            duration: 1,
          });
        }
      } else if (modalType === "return") {
        console.log(assetId);

        if (assetId) {
          const requestBody = {
            ...values,
            status: "1",
            statustext: "Avaliable",
            id: a,
            categoryId: assetById.category.id,
            model: assetById.model,
            name: assetById.name,
            price: assetById.price,
            purchaseDate: assetById.purchaseDate,
            quantity: assetById.quantity,
            serialNumber: assetById.serialNumber,
            unit: assetById.unit,
            year: assetById.year,
          };
          // Optionally include fixedAsset if needed
          if (values.fixedAsset) {
            requestBody.fixedAsset = { id: values.fixedAsset };
          }

          try {
            const response = await fetch(
              `http://localhost:6060/admin/updateFixedAsset/${assetId}`,
              {
                method: "PUT",
                headers,
                body: JSON.stringify(requestBody),
              }
            );

            if (response.ok) {
              fetchFixedAssets();
              notification.success({
                message: "Asset Updated",
                description: "Fixed asset has been updated successfully.",
                duration: 1,
              });
            } else {
              const result = await response.json();
              notification.error({
                message: "Update Failed",
                description:
                  result.message ||
                  "An error occurred while updating the fixed asset.",
                duration: 1,
              });
            }
          } catch (error) {
            console.error("Fetch error:", error);
            notification.error({
              message: "Update Failed",
              description: "An unexpected error occurred.",
              duration: 1,
            });
          }
        } else {
          notification.error({
            message: "Update Failed",
            description: "The asset ID is missing or invalid.",
            duration: 1,
          });
        }
      }

      // Close modal and reset form fields
      form.resetFields();
      setIsModalVisible(false);
    } catch (info) {
      console.log("Validate Failed:", info);
    }
  };

  const handleCancel = () => {
    setIsModalVisible(false);
    setIsViewModalVisible(false);
  };

  const handleEdit = (record) => {
    console.log(record.category.name);
    setModalType("fixedasset");
    setEditKey(record);
    form.setFieldsValue({
      name: record.name,
      categoryId: record.category.name,
      model: record.model,
      year: record.year,
      serialNumber: record.serialNumber,
      purchaseDate: record.purchaseDate ? moment(record.purchaseDate) : null,
      price: record.price,
      unit: record.unit,
      quantity: record.quantity,
    });
    setIsModalVisible(true);
  };

  const handleDelete = async (record) => {
    console.log("Deleting asset with key:", record.id);
    try {
      const token = localStorage.getItem("token");
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      await fetch(`http://localhost:6060/admin/deleteFixeAsset/${record.id}`, {
        method: "DELETE",
        headers,
      });
      console.log("Deleting asset with key:", record.key);
      setData((prevData) => prevData.filter((item) => item.id !== record.id));
      notification.success({
        message: "Asset Deleted",
        description: "Fixed asset has been deleted successfully.",
      });
    } catch (error) {
      console.error("Error deleting fixed asset:", error);
      notification.error({
        message: "Failed to delete asset",
        description: error.message,
      });
    }
  };

  const handleCategoryEdit = (category) => {
    setModalType("category");
    setEditCategory(category);
    form.setFieldsValue({
      name: category.name,
      description: category.description,
    });
    setIsModalVisible(true);
  };

  const handleCategoryDelete = async (category) => {
    try {
      const token = localStorage.getItem("token");
      const headers = {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      };
      await fetch(`http://localhost:6060/admin/categories/${category.id}`, {
        method: "DELETE",
        headers,
      });

      setCategories((prevCategories) =>
        prevCategories.filter((cat) => cat.id !== category.id)
      );

      notification.success({
        message: "Category Deleted",
        description: "Category has been deleted successfully.",
      });
    } catch (error) {
      console.error("Error deleting category:", error);
      notification.error({
        message: "Failed to delete category",
        description: error.message,
      });
    }
  };

  const menu = (
    <Menu>
      <Menu.Item key="1">
        <Button type="link" onClick={() => showModal("fixedasset")}>
          Create Fixed Asset
        </Button>
      </Menu.Item>
      <Menu.Item key="2">
        <Button type="link" onClick={() => showModal("category")}>
          Create Category
        </Button>
      </Menu.Item>
    </Menu>
  );

  const handleMenuClick = (e) => {
    setSelectedStatus(e.key);
    console.log(e.key);
  };

  const handleCategorySelect = (categoryId) => {
    setSelectedCategory(categoryId);
  };

  const filteredData = data.filter((item) => {
    const statusMatch = selectedStatus
      ? item.statustext === selectedStatus
      : true;
    const categoryMatch = selectedCategory
      ? item.category.id === selectedCategory
      : true;
    return statusMatch && categoryMatch;
  });

  const statusMenu = (
    <Menu onClick={handleMenuClick}>
      <Menu.Item key="In Use">In Use</Menu.Item>
      <Menu.Item key="Avaliable">Avaliable</Menu.Item>
    </Menu>
  );

  const categoryMenu = (
    <Menu>
      {categories.map((category) => (
        <Menu.Item
          key={category.id}
          onClick={() => handleCategorySelect(category.id)}
        >
          <div className="flex justify-between items-center w-full">
            <span>{category.name}</span>
            <div className="flex gap-2">
              <Button
                type="link"
                onClick={() => handleCategoryEdit(category)}
                icon={<EditOutlined />}
              />
              <Button
                type="link"
                icon={<DeleteOutlined />}
                onClick={() => handleCategoryDelete(category)}
              />
            </div>
          </div>
        </Menu.Item>
      ))}
    </Menu>
  );
  const handleViewHide = async (record, type) => {
    if (type === "view") {
      try {
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };
        console.log(record);
        const response = await fetch(
          // `http://localhost:6060/admin/getFixedAssetById/${record.id}`,
          `http://localhost:6060/admin/get_images_by_asset/${record.id}`,
          {
            method: "GET",
            headers,
          }
        );
        if (response.ok) {
          const assetDetails = await response.json();

          console.log(assetDetails);
          setViewAsset(assetDetails);
          setIsViewModalVisible(true);
        } else {
          const errorData = await response.json();
          notification.error({
            message: "Failed to Fetch Asset Details",
            description:
              errorData.message ||
              "There was an error fetching the asset details.",
          });
        }
      } catch (error) {
        notification.error({
          message: "Failed to Update Asset",
          description: "There was an error updating the asset.",
        });
        console.error("Error updating visibility:", error);
      }
     
    }
      // useEffect(() => {
  //   if (assetDetails && token) {
  //     handleViewHide(assetDetails, 'view', token); // Assuming you want to view asset details when record or token changes
  //   }
  // }, [assetDetails, token]);

     else if (type === "hide") {
      try {
        const headers = {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`,
        };
        const response = await fetch(
          `http://localhost:6060/admin/updateFixedAsset/${record.id}`,
          {
            method: "PUT",
            headers,
            body: JSON.stringify({
              ...record,
              categoryId: record.category.id,
              status: "0",
            }),
          }
        );

        if (response.ok) {
          const updatedRecord = await response.json();
          // console.log('Updated Record:', updatedRecord);
          await fetchFixedAssets();

          notification.success({
            message: "Asset  Updated",
            description: `Asset "${updatedRecord.fixedAsset.name}" has been updated.`,
          });
        } else {
          const errorData = await response.json();
          notification.error({
            message: "Failed to Update asset",
            description:
              errorData.message || "There was an error updating the asset .",
          });
        }
      } catch (error) {
        notification.error({
          message: "Failed to Update asset",
          description: "There was an error updating the asset.",
        });
        console.error("Error updating visibility:", error);
      }
    }
  };

  // useEffect(() => {
  //   if (assetDetails && token) {
  //     handleViewHide(assetDetails, 'view', token); // Assuming you want to view asset details when record or token changes
  //   }
  // }, [assetDetails, token]);

 
  const handleRefresh = async () => {
    setLoading(true);
    try {
      await fetchFixedAssets(); // Call the function to refresh data
    } catch (error) {
      console.error("Failed to refresh data", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <>
      <div className="flex flex-col h-screen ">
        {/* Button Groups */}
        <div className="flex justify-between items-center ">
          <div className="flex">
            <Dropdown overlay={menu} placement="bottomLeft">
              <Button
                size="middle"
                className="bg-yellow-500 hover:bg-white text-white"
              >
                Create <DownOutlined />
              </Button>
            </Dropdown>

            <Dropdown overlay={categoryMenu} placement="bottomLeft">
              <Button size="middle" style={{ marginLeft: 8 }}>
                Categories <DownOutlined />
              </Button>
            </Dropdown>

            <Dropdown overlay={statusMenu} placement="bottomLeft">
              <Button
                size="middle"
                style={{ marginLeft: 8 }}
                className="hover:bg-yellow-50 text-gray"
              >
                Status <DownOutlined />
              </Button>
            </Dropdown>

            <Button
              size="middle"
              style={{ marginLeft: 8 }}
              shape="circle"
              icon={<PlusOutlined />}
              onClick={() => {
                setIsModalVisible(true);
                setModalType("assign");
              }}
            />
          </div>

          <div className="flex">
            <Button
              size="middle"
              style={{ marginLeft: 8 }}
              icon={<DownloadOutlined />}
              // onClick={handleDownload}
            >
              Download
            </Button>

            <Button
              size="middle"
              style={{ marginLeft: 8 }}
              icon={<ReloadOutlined />}
              onClick={handleRefresh}
            >
              Refresh
            </Button>
          </div>
        </div>

        {/* Table */}
        <div className="flex-1 overflow-auto">
          <Table
            columns={columns(handleEdit, handleDelete, handleViewHide)}
            dataSource={filteredData}
            className="mt-5"
          />
        </div>
      </div>

      <Modal
        title={
          modalType === "category"
            ? editCategory
              ? "Edit Category"
              : "Create Category"
            : modalType === "fixedasset"
            ? editKey
              ? "Edit Fixed Asset"
              : "Create Fixed Asset"
            : "Assign Fixed Asset"
        }
        visible={isModalVisible}
        onOk={handleOk}
        onCancel={handleCancel}
        cancelText="Cancel"
      >
        <Form form={form} layout="vertical" initialValues={{ remember: true }}>
          {modalType === "fixedasset" && (
            <>
              <Form.Item
                name="name"
                label="Asset Name"
                rules={[
                  { required: true, message: "Please input the asset name!" },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="categoryId"
                label="Category"
                rules={[
                  { required: true, message: "Please select a category!" },
                ]}
              >
                <Select>
                  {categories.map((category) => (
                    <Option key={category.id} value={category.id}>
                      {category.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
              <Form.Item
                name="model"
                label="Model"
                rules={[{ required: true, message: "Please input the model!" }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="year"
                label="Year"
                rules={[{ required: true, message: "Please input the year!" }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="serialNumber"
                label="Serial Number"
                rules={[
                  {
                    required: true,
                    message: "Please input the serial number!",
                  },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="purchaseDate"
                label="Purchase Date"
                rules={[
                  {
                    required: true,
                    message: "Please select the purchase date!",
                  },
                ]}
              >
                <DatePicker />
              </Form.Item>
              <Form.Item
                name="price"
                label="Price"
                rules={[{ required: true, message: "Please input the price!" }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="unit"
                label="Unit"
                rules={[{ required: true, message: "Please input the unit!" }]}
              >
                <Input />
              </Form.Item>
              <Form.Item
                name="quantity"
                label="Quantity"
                rules={[
                  { required: true, message: "Please input the quantity!" },
                ]}
              >
                <Input />
              </Form.Item>
              <Form.Item label="Upload Image">
                <input
                  type="file"
                  multiple
                  onChange={(e) => setImages([...e.target.files])}
                />
              </Form.Item>
            </>
          )}
          {modalType === "category" && (
            <Form.Item
              name="name"
              label="Category Name"
              rules={[
                { required: true, message: "Please input the category name!" },
              ]}
            >
              <Input />
            </Form.Item>
          )}
          {modalType === "assign" && (
            <>
              <Form.Item
                name="id"
                label="Select Fixed Asset"
                rules={[
                  { required: true, message: "Please select a fixed asset!" },
                ]}
              >
                <Select
                  showSearch
                  placeholder="Select a fixed asset"
                  onChange={(e) => setA(e)}
                  optionFilterProp="children"
                  filterOption={(input, option) =>
                    option?.children.toLowerCase().includes(input.toLowerCase())
                  }
                  suffixIcon={<SearchOutlined />}
                >
                  {data.map((fixedAsset) => (
                    <Option key={fixedAsset.id} value={fixedAsset.id}>
                      {fixedAsset.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>

              <Form.Item
                name="assetHolder"
                label="Select Asset Holder"
                rules={[
                  { required: true, message: "Please select an asset holder!" },
                ]}
              >
                <Select
                  showSearch
                  placeholder="Select an asset holder"
                  optionFilterProp="children"
                  filterOption={(input, option) =>
                    option?.children.toLowerCase().includes(input.toLowerCase())
                  }
                  suffixIcon={<SearchOutlined />}
                >
                  {assetHolders.map((holder) => (
                    <Option key={holder.id} value={holder.id}>
                      {holder.name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>

              <Form.Item
                name="purchaseDate"
                label="Purchase Date"
                rules={[
                  {
                    required: true,
                    message: "Please select the purchase date!",
                  },
                ]}
              >
                <DatePicker />
              </Form.Item>
            </>
          )}

          {modalType === "return" && (
            <p>Are you sure you want to return this asset?</p>
          )}
        </Form>

        {/* } */}
      </Modal>
      <Modal
        visible={isViewModalVisible}
        onOk={handleCancel}
        onCancel={handleCancel}
        cancelText="Cancel"
        width={800}
      >
        <div className="p-8 border rounded-lg bg-gradient-to-r from-blue-50 to-white shadow-xl">
          <h2 className="text-3xl font-semibold text-yellow-500 mb-8 text-center">
            Asset Details
          </h2>
          <div className="mb-8 text-center">
            {assetDetails?.files?.map((file, index) => (
              <img
                key={index}
                src={file.fileUrl}
                alt={file.fileName}
                className="w-100 h-100 mx-auto rounded-lg border border-gray-300 shadow-md object-cover"
                style={{ width: "100px", height: "100px" }}
              />
            ))}
          </div>
          {/* Grid Layout */}
          <div className="grid grid-cols-2 gap-6">
            {/* Grid Item */}
            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold w-1/3 pr-4">No:</div>
              <div className="text-gray-800 w-2/3">
                {assetDetails?.fixedAssetId}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Name:</div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetName}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Category:</div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetCategory}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Model:</div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetModel}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Year:</div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetYear}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">
                Serial Number:
              </div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetSerialNumber}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">
                Purchase Date:
              </div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetPurchaseDate}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Price ($):</div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetPrice}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Unit:</div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetUnit}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Quantity:</div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetQuantity}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">Status:</div>
              <div
                className={`text-gray-800 ${
                  assetDetails?.fixedAssetStatusText === "Available"
                    ? "text-green-600"
                    : assetDetails?.fixedAssetStatusText === "In Use"
                    ? "text-red-600"
                    : ""
                }`}
              >
                {assetDetails?.fixedAssetStatusText}
              </div>
            </div>

            <div className="bg-white p-4 rounded-lg shadow-md border border-gray-200 flex items-center">
              <div className="text-gray-700 font-semibold mr-4">
                Asset Holder:
              </div>
              <div className="text-gray-800">
                {assetDetails?.fixedAssetAssetHolder}
              </div>
            </div>
          </div>
        </div>
      </Modal>
    </>
  );
};

export default TotalAsset;
