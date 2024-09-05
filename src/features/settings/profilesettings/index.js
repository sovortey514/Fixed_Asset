import moment from "moment";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import TitleCard from "../../../components/Cards/TitleCard";
import { showNotification } from "../../common/headerSlice";
import InputText from "../../../components/Input/InputText";
import TextAreaInput from "../../../components/Input/TextAreaInput";
import ToogleInput from "../../../components/Input/ToogleInput";

function ProfileSettings() {
  const dispatch = useDispatch();

  // Call API to update profile settings changes
  const updateProfile = () => {
    dispatch(showNotification({ message: "Profile Updated", status: 1 }));
  };

  const updateFormValue = ({ updateType, value }) => {
    console.log(updateType);
  };

  const InputText = ({ labelTitle, defaultValue, updateFormValue }) => (
    <div className="flex flex-col">
      <label className="text-sm font-medium text-gray-600 mb-1">{labelTitle}</label>
      <input
        type="text"
        defaultValue={defaultValue}
        onChange={(e) => updateFormValue(labelTitle, e.target.value)}
        className="border border-gray-300 p-2 rounded-lg shadow-sm focus:outline-none focus:ring-2 focus:ring-yellow-400 transition duration-150 ease-in-out"
      />
    </div>
  );

  const TitleCard = ({ title, topMargin, children }) => (
    <div className={`bg-white shadow-lg rounded-lg p-6 ${topMargin}`}>
      <h1 className="text-2xl font-bold text-gray-800 mb-4">{title}</h1>
      {children}
    </div>
  );
  const defaultImage = 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKJQp8ndvEkIa-u1rMgJxVc7BBsR11uSLHGA&s';
  const [profileImage, setProfileImage] = useState('https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQKJQp8ndvEkIa-u1rMgJxVc7BBsR11uSLHGA&s');
  const handleImageError = () => {
    setProfileImage(defaultImage);
  };
  return (
    <>
      <TitleCard  topMargin="mt-4">
      {/* Profile Image Section */}
      <div className="flex flex-col items-center mb-8">
      <div className="relative group">
      <img
        src={profileImage}
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
        <h2 className="text-2xl mt-4 font-semibold text-gray-800">Admin</h2>
      </div>

      {/* Form Fields */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-8 mb-8">
        <InputText
          labelTitle="Name"
          defaultValue="Admin"
          updateFormValue={updateFormValue}
        />
        <InputText
          labelTitle="Email"
          defaultValue="admin@gmail.com"
          updateFormValue={updateFormValue}
        />
        <InputText
          labelTitle="Department"
          defaultValue="OFL"
          updateFormValue={updateFormValue}
        />
        <InputText
          labelTitle="Language"
          defaultValue="English"
          updateFormValue={updateFormValue}
        />
      </div>

      {/* Divider */}
      <div className="border-t border-gray-300 mt-6 mb-8"></div>

      {/* Save Button */}
      <div className="flex justify-end">
        <button
          className="bg-yellow-500 text-white font-semibold py-2 px-6 rounded-lg shadow-md transition-colors duration-300 ease-in-out hover:bg-yellow-600"
          onClick={updateProfile}
        >
          Update
        </button>
      </div>
    </TitleCard>
    </>
  );
}

export default ProfileSettings;
