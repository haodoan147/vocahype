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
CREATE SCHEMA IF NOT EXISTS heroku_ext;

CREATE EXTENSION IF NOT EXISTS citext schema heroku_ext;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: vh; Owner: vh
--

CREATE SEQUENCE IF NOT EXISTS vh.roles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: roles; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.roles (
    id integer NOT NULL DEFAULT nextval('vh.roles_id_seq'::regclass),
    title text NOT NULL,
    created_on timestamp with time zone DEFAULT now() NOT NULL,
    updated_on timestamp with time zone,
    CONSTRAINT roles_pkey PRIMARY KEY (id)
    );


--
-- Name: users; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.users (
    id text NOT NULL,
    role_id integer DEFAULT 1 NOT NULL,
    login_name heroku_ext.citext NOT NULL,
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
    goal_seconds integer DEFAULT 300,
    CONSTRAINT users_pkey PRIMARY KEY (id),
    CONSTRAINT users_roles_id_fk FOREIGN KEY (role_id) REFERENCES vh.roles(id)
    );

--
-- Name: words; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.pos (
                                        pos_tag text NOT NULL ,
                                        description text NOT NULL ,
                                        CONSTRAINT pos_pkey PRIMARY KEY (pos_tag)
);

--
-- Name: seq_id_word; Type: SEQUENCE; Schema: vh; Owner: vh
--

CREATE SEQUENCE IF NOT EXISTS vh.seq_id_words
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: words; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.words (
                                        id integer NOT NULL DEFAULT nextval('vh.seq_id_words'::regclass),
                                        word text NOT NULL,
                                        count bigint,
                                        pos_tag text,
                                        point double precision,
                                        phonetic text,
                                        syllables integer,
                                        phonetic_start text,
                                        phonetic_end text,
                                        created_on timestamp with time zone DEFAULT now() NOT NULL,
                                        updated_on timestamp with time zone,
                                        CONSTRAINT words_pkey PRIMARY KEY (id),
                                        CONSTRAINT words_pos_pos_tag_fk FOREIGN KEY (pos_tag) REFERENCES vh.pos(pos_tag)
);

--
-- Name: words_user_knowledge; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.words_user_knowledge (
    user_id text NOT NULL,
    word_id integer NOT NULL,
    status boolean default false,
    CONSTRAINT words_user_knowledge_pkey PRIMARY KEY (user_id, word_id),
    CONSTRAINT words_user_knowledge_user_id_fk FOREIGN KEY (user_id) REFERENCES vh.users(id),
    CONSTRAINT words_user_knowledge_word_id_fk FOREIGN KEY (word_id) REFERENCES vh.words(id)
);

--
-- Name: seq_id_definition; Type: SEQUENCE; Schema: vh; Owner: vh
--

CREATE SEQUENCE IF NOT EXISTS vh.seq_id_definition
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: words; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.definition (
                                        id integer NOT NULL DEFAULT nextval('vh.seq_id_definition'::regclass),
                                        word_id integer,
                                        definition text NOT NULL,
                                        CONSTRAINT definition_pkey PRIMARY KEY (id),
                                        CONSTRAINT definition_words_id_fk FOREIGN KEY (word_id) REFERENCES vh.words(id)
);

--
-- Name: seq_id_definition; Type: SEQUENCE; Schema: vh; Owner: vh
--

CREATE SEQUENCE IF NOT EXISTS vh.seq_id_examples
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: words; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.examples (
                                           id integer NOT NULL DEFAULT nextval('vh.seq_id_examples'::regclass),
                                           definition_id integer,
                                           example text NOT NULL,
                                           CONSTRAINT examples_pkey PRIMARY KEY (id),
                                           CONSTRAINT examples_definition_id_fk FOREIGN KEY (definition_id) REFERENCES vh.definition(id)
);

--
-- Name: words; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.synonyms (
                                           word_id integer,
                                           synonym_id integer,
                                           is_synonym boolean default true,
                                           CONSTRAINT synonyms_pkey PRIMARY KEY (word_id, synonym_id),
                                           CONSTRAINT synonyms_words_id_fk FOREIGN KEY (word_id) REFERENCES vh.words(id),
                                           CONSTRAINT synonyms_all_words_id_fk FOREIGN KEY (synonym_id) REFERENCES vh.words(id)
);

--
-- Name: seq_id_word_comprehension_levels; Type: SEQUENCE; Schema: vh; Owner: vh
--

CREATE SEQUENCE IF NOT EXISTS vh.seq_id_word_comprehension_levels
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

--
-- Name: word_comprehension_levels; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS vh.word_comprehension_levels (
                                           id integer NOT NULL DEFAULT nextval('vh.seq_id_word_comprehension_levels'::regclass),
                                           reinforce_interval interval DEFAULT '1 day'::interval,
                                           description text,
                                           CONSTRAINT word_comprehension_levels_pkey PRIMARY KEY (id)
);
--
-- --
-- -- Name: seq_id_word_comprehension_levels; Type: SEQUENCE; Schema: vh; Owner: vh
-- --
--
-- CREATE SEQUENCE IF NOT EXISTS vh.seq_id_user_profile
--     START WITH 1
--     INCREMENT BY 1
--     NO MINVALUE
--     NO MAXVALUE
--     CACHE 1;
--
-- --
-- -- Name: word_comprehension_levels; Type: TABLE; Schema: vh; Owner: vh
-- --
--
-- CREATE TABLE IF NOT EXISTS vh.user_profile (
--                                                             id integer NOT NULL DEFAULT nextval('vh.seq_id_user_profile'::regclass),
--                                                             user_id text NOT NULL,
--                                                             goal_seconds integer DEFAULT 300,
--                                                             CONSTRAINT user_profile_pkey PRIMARY KEY (id),
--                                                             CONSTRAINT user_profile_user_id_fk FOREIGN KEY (user_id) REFERENCES vh.users(id)
-- );