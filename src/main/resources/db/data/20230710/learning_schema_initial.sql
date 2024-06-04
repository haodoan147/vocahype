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


CREATE SCHEMA IF NOT EXISTS learning;
--
-- Name: meter_x_insert(); Type: FUNCTION; Schema: readings; Owner: its
--
--
-- CREATE OR REPLACE FUNCTION learning.user_word_comprehension_x_insert() RETURNS trigger
--     LANGUAGE plpgsql
-- AS $_$
-- BEGIN
--     EXECUTE format('INSERT INTO learning.user_word_comprehension_%s VALUES ($1.*) ON CONFLICT
-- (user_id, word_id) DO NOTHING',
-- --                        ||
-- --                    'DO UPDATE SET date_updated = $1.date_updated',
-- --                    NEW.user_id / 1000) USING NEW;
-- --     RETURN NULL;
-- -- END;
--
--                    NEW.user_id / 1000) USING NEW;
--     RETURN NULL;
-- END;
-- $_$;


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: user_word_comprehension; Type: TABLE; Schema: learning; Owner: vh
--

CREATE TABLE IF NOT EXISTS learning.user_word_comprehension (
                                              user_id text NOT NULL,
                                              word_id integer NOT NULL,
                                              word_comprehension_levels_id integer NOT NULL,
                                              next_learning date DEFAULT date_trunc('day',(now() + '1 day'::interval)),
    CONSTRAINT user_word_comprehension_user_id_word_id_pk PRIMARY KEY (user_id, word_id),
    CONSTRAINT user_word_comprehension_word_id_word_id_fk FOREIGN KEY (word_id) REFERENCES vh.words(id)
);

--
-- Name: fki_meter_unit_id_unit_id_fk; Type: INDEX; Schema: readings; Owner: its
--

CREATE INDEX IF NOT EXISTS fki_user_word_comprehension_user_id_user_id_fk ON learning.user_word_comprehension USING btree (user_id);


--
-- Name: fki_meter_unit_id_unit_id_fk; Type: INDEX; Schema: readings; Owner: its
--

CREATE INDEX IF NOT EXISTS fki_user_word_comprehension_word_id_word_id_fk ON learning.user_word_comprehension USING btree (word_id);

--
-- Name: meter_status_date_created_idx; Type: INDEX; Schema: readings; Owner: its
--

CREATE INDEX IF NOT EXISTS fki_user_word_comprehension_word_id_user_id_idx ON learning.user_word_comprehension USING btree (user_id, word_id);

--
-- Name: meter readings_meter_on_insert; Type: TRIGGER; Schema: readings; Owner: its
--

-- DROP TRIGGER IF EXISTS learning_user_word_comprehension_on_insert ON learning.user_word_comprehension;
--
-- CREATE TRIGGER learning_user_word_comprehension_on_insert BEFORE INSERT ON learning.user_word_comprehension FOR EACH ROW EXECUTE FUNCTION learning.user_word_comprehension_x_insert();


--
-- Name: word_comprehension_levels; Type: TABLE; Schema: vh; Owner: vh
--

CREATE TABLE IF NOT EXISTS learning.user_learning_goal_tracking (
    user_id text NOT NULL,
    date_learn date NOT NULL DEFAULT now(),
    user_learnt_time integer,
    CONSTRAINT user_learning_goal_tracking_pkey PRIMARY KEY (user_id, date_learn),
    CONSTRAINT user_learning_goal_tracking_user_id_fk FOREIGN KEY (user_id) REFERENCES vh.users(id)
    );