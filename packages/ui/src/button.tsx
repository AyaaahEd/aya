"use client";

import { ReactNode } from "react";

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  className?: string;
  appName?: string;
}

export const Button = ({ children, className, appName, ...props }: ButtonProps) => {
  return (
    <button
      className={`px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors ${className}`}
      {...props}
    >
      {children}
    </button>
  );
};
