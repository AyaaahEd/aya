"use client";

import { useState } from "react";
import { registerUser } from "../../lib/api";
import { useRouter } from "next/navigation";

export default function RegisterPage() {
    const [formData, setFormData] = useState({
        username: "",
        password: "",
        email: "",
        fullName: ""
    });
    const [error, setError] = useState("");
    const router = useRouter();

    const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError("");
        try {
            await registerUser({
                username: formData.username,
                password: formData.password,
                email: formData.email,
                roles: ["ROLE_CUSTOMER"] // Default role
            });
            alert("Registration successful!");
            router.push("/");
        } catch (err: any) {
            console.error(err);
            setError(err.message || "Registration failed");
        }
    };

    return (
        <main className="flex min-h-screen flex-col items-center justify-center p-24 bg-gray-50">
            <div className="bg-white p-8 rounded-lg shadow-lg max-w-md w-full">
                <h1 className="text-2xl font-bold mb-6 text-center">Create Account</h1>

                {error && (
                    <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4 text-sm">
                        {error}
                    </div>
                )}

                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label className="block text-sm font-medium text-gray-700">Username</label>
                        <input
                            name="username"
                            type="text"
                            required
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            onChange={handleChange}
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Email</label>
                        <input
                            name="email"
                            type="email"
                            required
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            onChange={handleChange}
                        />
                    </div>

                    <div>
                        <label className="block text-sm font-medium text-gray-700">Password</label>
                        <input
                            name="password"
                            type="password"
                            required
                            className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2"
                            onChange={handleChange}
                        />
                    </div>

                    <button
                        type="submit"
                        className="w-full bg-blue-600 text-white py-2 px-4 rounded hover:bg-blue-700 transition font-medium"
                    >
                        Register
                    </button>
                </form>
                <p className="mt-4 text-center text-sm text-gray-600">
                    Already have an account? <a href="/" className="text-blue-600 hover:underline">Login</a>
                </p>
            </div>
        </main>
    );
}
