--
-- PostgreSQL database dump
--

-- Dumped from database version 12.11
-- Dumped by pg_dump version 13.10

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: vh; Type: SCHEMA; Schema: -; Owner: vh
--

CREATE SCHEMA IF NOT EXISTS vh;


ALTER SCHEMA vh OWNER TO vh;

CREATE EXTENSION IF NOT EXISTS citext schema vh;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: vh; Owner: vh
--

CREATE SEQUENCE IF NOT EXISTS vh.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: role; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.role (
    id integer NOT NULL DEFAULT nextval('vh.role_id_seq'::regclass),
    bitmask integer NOT NULL,
    title text NOT NULL,
    created_on timestamp with time zone DEFAULT now() NOT NULL,
    updated_on timestamp with time zone,
                             CONSTRAINT role_pkey PRIMARY KEY (id)
    );


ALTER TABLE vh.role OWNER TO vh;


ALTER TABLE vh.role_id_seq OWNER TO vh;

--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: vh; Owner: vh
--

ALTER SEQUENCE vh.role_id_seq OWNED BY vh.role.id;


--
-- Name: seq_id_users; Type: SEQUENCE; Schema: vh; Owner: vh
--

CREATE SEQUENCE IF NOT EXISTS vh.seq_id_users
    START WITH 600000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: users; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.users (
    id integer NOT NULL DEFAULT nextval('vh.seq_id_users'::regclass),
    role_id integer DEFAULT 1 NOT NULL,
    login_name vh.citext NOT NULL,
    secret text,
    encryption character varying(16) DEFAULT 'None'::character varying,
    first_name text DEFAULT ''::text NOT NULL,
    last_name text DEFAULT ''::text NOT NULL,
    birthday text,
    gender text,
    timezone text DEFAULT 'GMT'::text,
    locale character varying(16) DEFAULT 'en_GB'::character varying,
    status integer DEFAULT 0 NOT NULL,
    login_count integer DEFAULT 0 NOT NULL,
    created_on timestamp with time zone DEFAULT now() NOT NULL,
    updated_on timestamp with time zone,
                             CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_role_id_fk FOREIGN KEY (role_id) REFERENCES vh.role(id)
    );


ALTER TABLE vh.users OWNER TO vh;


ALTER TABLE vh.seq_id_users OWNER TO vh;

--
-- Name: seq_id_users; Type: SEQUENCE OWNED BY; Schema: vh; Owner: vh
--

ALTER SEQUENCE vh.seq_id_users OWNED BY vh.users.id;

