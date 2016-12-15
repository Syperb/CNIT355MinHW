package com.example.cnit355.minhw;

/**
 * Created by Brad on 12/14/2016.
 */


    public class tasks
    {
        private String name, due;

        public tasks(){}

        public tasks( String n, String d)
        {
            this.name = n;
            this.due = d;
        }

        public String getName()
        {
            return name;
        }

        public String getDue()
        {
            return due;
        }

        public void setName (String n)
        {
            this.name = n;
        }

        public void setDue (String d)
        {
            this.due = d;
        }
    }

