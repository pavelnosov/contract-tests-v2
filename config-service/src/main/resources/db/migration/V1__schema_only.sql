--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';


--
-- Name: constraint_constraintvalues; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE configs (
    id character varying(255) NOT NULL primary key,
    name character varying(255) NOT NULL,
    value TEXT NOT NULL,
    created_at timestamp with time zone default now() not null,
    updated_at timestamp with time zone default now() not null
);

