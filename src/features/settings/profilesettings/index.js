import moment from "moment"
import { useEffect, useState } from "react"
import { useDispatch, useSelector } from "react-redux"
import TitleCard from "../../../components/Cards/TitleCard"
import { showNotification } from '../../common/headerSlice'
import InputText from '../../../components/Input/InputText'
import TextAreaInput from '../../../components/Input/TextAreaInput'
import ToogleInput from '../../../components/Input/ToogleInput'

function ProfileSettings(){

    const dispatch = useDispatch()

    // Call API to update profile settings changes
    const updateProfile = () => {
        dispatch(showNotification({message : "Profile Updated", status : 1}))    
    }

    const updateFormValue = ({updateType, value}) => {
        console.log(updateType)
    }

    return(
        <>
            <TitleCard title="Profile Settings" topMargin="mt-4">
    {/* Profile Image Section */}
    <div className="flex flex-col items-center mb-6">
        <div className="relative group">
            <img
                src="https://via.placeholder.com/150"
                alt="Profile"
                className="w-28 h-28 rounded-full border-4 border-gray-200 shadow-lg transform transition-transform duration-300 ease-in-out hover:scale-110"
            />
            {/* Edit Button */}
            <button
                className="absolute bottom-0 right-0 bg-white text-yellow-500 p-1.5 rounded-full shadow-lg transition-colors duration-300 ease-in-out hover:text-yellow-600"
                aria-label="Edit Profile Picture"
            >
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    className="h-5 w-5"
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
        <h2 className="text-xl mt-3 font-semibold text-gray-700">Alex</h2>
    </div>

    {/* Form Fields */}
    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <InputText labelTitle="Name" defaultValue="Alex" updateFormValue={updateFormValue} />
        <InputText labelTitle="Email" defaultValue="admin@gmail.com" updateFormValue={updateFormValue} />
        <InputText labelTitle="Department" defaultValue="OFL" updateFormValue={updateFormValue} />
        <InputText labelTitle="Language" defaultValue="English" updateFormValue={updateFormValue} />
    </div>

    {/* Divider */}
    <div className="border-t mt-6 mb-6"></div>

    {/* Save Button */}
    <div className="flex justify-end">
        <button
            className="bg-yellow-500 text-white font-semibold py-1.5 px-4 rounded-md shadow-md transition-colors duration-300 ease-in-out "
            onClick={updateProfile}
        >
            Update
        </button>
    </div>
</TitleCard>

        </>
    )
}

export default ProfileSettings
