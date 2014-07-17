/*******************************************************************************
 * Copyright (c) 2012 Claudio Antares Mezzina.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 *     Claudio Antares Mezzina - initial API and implementation
 ******************************************************************************/
package language.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import language.value.IValue;
import language.value.expression.SimpleId;

public class Channel implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -8231922135871012004L;
    // the first element in the channel is the last one inserted

    // value, sender
    protected LinkedList<Tuple<IValue, String>> value;
    protected LinkedList<Integer> pc_sender;
    // value, sender, receiver
    protected LinkedList<Tuple<Tuple<IValue, String>, String>> story;
    protected LinkedList<Integer> pc_reader;

    static public boolean NO_MEMORY = false;

    // pair of thread_id and boolean
    // true means it is a reader, false it is a sender
    protected ArrayList<Tuple<String, Boolean>> sched;

    public Channel() {
        value = new LinkedList<Tuple<IValue, String>>();
        sched = new ArrayList<Tuple<String, Boolean>>();
        if (!NO_MEMORY) {
            story = new LinkedList<Tuple<Tuple<IValue, String>, String>>();
            pc_sender = new LinkedList<Integer>();
            pc_reader = new LinkedList<Integer>();
        }
    }

    public boolean isEmpty() {
        return value.isEmpty();
    }

    public boolean isEmptyHistory() {
        return story.isEmpty();
    }

    public void send(IValue val, String thread, int gamma) {
        value.add(new Tuple<IValue, String>(val, thread));
        sched.add(new Tuple<String, Boolean>(thread, false));

        if (!NO_MEMORY) {
            pc_sender.add(gamma);
            // System.out.println(" ... inserted value "+gamma
            // +" by thread "+thread );
        }
    }

    public IValue receive(String thread, int gamma) {
        if (value.isEmpty()) {
            return null;
        }

        Tuple<IValue, String> ret = value.removeFirst();
        if (!NO_MEMORY) {
            pc_reader.addFirst(gamma);
            story.addFirst(new Tuple<Tuple<IValue, String>, String>(ret, thread));
        }
        // System.out.println(" ... reading "+thread +" "+gamma );

        sched.add(new Tuple<String, Boolean>(thread, true));
        return ret.getFirst();
    }

    // removes the tail of the channel (reverse send)
    public IValue reverseSend(String thread) {
        if (value.isEmpty()) {
            return null;
        }
        Tuple<IValue, String> head = value.getLast();
        // the head of the channel belongs to the thread
        if (head.getSecond().equals(thread)) {
            value.removeLast();
            pc_sender.removeLast();
            // int i=pc_sender.removeLast();
            // System.out.println(i);
            return head.getFirst();
        } else {
            return null;
        }

    }

    public ArrayList<Tuple<String, Boolean>> getIOAccessSequence() {
        return sched;
    }

    public boolean reverseReceive(String thread) {
        if (story.isEmpty()) {
            return false;
        }
        // the receiver of the top (tuple) of the history shold be = thread
        Tuple<Tuple<IValue, String>, String> log = story.getFirst();
        if (log.getSecond().equals(thread)) {
            story.remove();
            pc_reader.remove();
            value.addFirst(log.getFirst());
            return true;
        }
        return false;
    }

    public List<Tuple<IValue, String>> getValues() {
        return value;
    }

    public LinkedList<Tuple<Tuple<IValue, String>, String>> getStory() {
        return story;
    }

    public boolean emptyStory() {
        if (NO_MEMORY) {
            return true;
        }
        return story.isEmpty();
    }

    // returns a hashmap thread_id, gamma indicating that all the threads should
    // roll till their gamma
    public HashMap<String, Integer> getReaders(String thread) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        Iterator<Tuple<Tuple<IValue, String>, String>> it = story.iterator();
        LinkedList<Integer> tmp_reader = new LinkedList<Integer>(pc_reader);

        while (it.hasNext()) {
            Tuple<Tuple<IValue, String>, String> val = it.next();
            // int gamma = tmp_reader.removeLast();
            int gamma = tmp_reader.remove(0);

            String sender = val.getFirst().getSecond();

            // first occurrence
            if (!ret.containsKey(val.getSecond())
                    || ret.get(val.getSecond()) > gamma) {
                ret.put(val.getSecond(), gamma);
            }

            // ret.add(val.getSecond());
            if (sender.equals(thread)) {
                break;
            }

        }
        return ret;
    }

    public HashMap<String, Integer> getbeforeMe(String thread) {
        // should reverse all the communication before
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        Iterator<Tuple<Tuple<IValue, String>, String>> it = story.iterator();
        LinkedList<Integer> tmp_reader = new LinkedList<Integer>(pc_reader);

        while (it.hasNext()) {
            Tuple<Tuple<IValue, String>, String> val = it.next();
            int gamma = tmp_reader.remove();

            String receiver = val.getSecond();

            if (receiver.equals(thread)) {
                break;
            }

            // first occurrence
            if (!ret.containsKey(val.getSecond())
                    || ret.get(val.getSecond()) > gamma) {
                ret.put(val.getSecond(), gamma);
            }

            // ret.add(val.getSecond());

        }
        return ret;
    }

    public HashMap<String, Integer> getSenders(String thread) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        Tuple<IValue, String> val;
        LinkedList<Integer> tmp_sender = new LinkedList<Integer>(pc_sender);
        Iterator<Tuple<IValue, String>> it = value.descendingIterator();
        while (it.hasNext()) {
            val = it.next();
            if (val.getSecond().equals(thread)) {
                return ret;
            } else {
                ret.put(val.getSecond(), tmp_sender.removeLast());
            }
        }
        return ret;
    }

    public HashMap<String, Integer> getlastNRead(int n) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        Iterator<Tuple<Tuple<IValue, String>, String>> it = story.iterator();
        LinkedList<Integer> tmp_reader = new LinkedList<Integer>(pc_reader);

        while (it.hasNext()) {
            if (n == 0) {
                break;
            }

            Tuple<Tuple<IValue, String>, String> val = it.next();
            // int gamma = tmp_reader.removeLast();
            int gamma = tmp_reader.remove(0);

            if (!ret.containsKey(val.getSecond())
                    || ret.get(val.getSecond()) > gamma) {
                ret.put(val.getSecond(), gamma);
            }

            n--;
        }
        return ret;

    }

    public HashMap<String, Integer> getlastNSend(int n) {
        HashMap<String, Integer> ret = new HashMap<String, Integer>();
        Tuple<IValue, String> val;
        LinkedList<Integer> tmp_sender = new LinkedList<Integer>(pc_sender);
        Iterator<Tuple<IValue, String>> it = value.descendingIterator();
        while (it.hasNext()) {
            if (n == 0) {
                break;
            }

            val = it.next();
            ret.put(val.getSecond(), tmp_sender.removeLast());
            n--;
        }

        return ret;
    }

    public void printSched() {
        Iterator<Tuple<String, Boolean>> it = sched.iterator();
        while (it.hasNext()) {
            Tuple<String, Boolean> val = it.next();
            System.out
                    .print("(" + val.getFirst() + " " + val.getSecond() + ")");
        }
        System.out.println();

    }

    // for testing purposes
    public static void main(String args[]) {
        Channel ch = new Channel();
        ch.send(new SimpleId("pippo"), "t3", 4);
        ch.send(new SimpleId("pluto"), "t2", 1);
        ch.send(new SimpleId("paperino"), "t1", 2);

        // System.out.println(ch.reverseSend("t1"));
        /* System.out.println(ch.getValues()); */

        System.out.println(ch.receive("t4", 18));
        System.out.println(ch.receive("t3", 12));

        /*
         * System.out.println(ch.getReaders("t1"));
         * System.out.println(ch.reverseReceive("t4"));
         * System.out.println(ch.reverseReceive("t4"));
         */
        /*
         * System.out.println(ch.story); Channel cl = ch.clone();
         * System.out.println(cl.story);
         */
        ch.printSched();

    }

    @Override
    @SuppressWarnings("unchecked")
    public Channel clone() {
        Channel ch = new Channel();
        LinkedList<Integer> clone = (LinkedList<Integer>) this.pc_reader
                .clone();
        ch.pc_reader = clone;
        ch.pc_sender = (LinkedList<Integer>) this.pc_sender.clone();

        /* cloning channel VALUE with the SAME exact order */
        LinkedList<Tuple<IValue, String>> cloned_val = new LinkedList<Tuple<IValue, String>>();
        Iterator<Tuple<IValue, String>> it = this.value.iterator();
        while (it.hasNext()) {
            Tuple<IValue, String> tp = it.next();
            cloned_val.addLast(new Tuple<IValue, String>(tp.getFirst().clone(),
                    tp.getSecond()));
        }

        /* cloning channel HISTORY */
        Iterator<Tuple<Tuple<IValue, String>, String>> iter = this.story
                .iterator();
        LinkedList<Tuple<Tuple<IValue, String>, String>> cloned_story = new LinkedList<Tuple<Tuple<IValue, String>, String>>();

        while (iter.hasNext()) {
            Tuple<Tuple<IValue, String>, String> tp = iter.next();
            Tuple<Tuple<IValue, String>, String> cloned = new Tuple<Tuple<IValue, String>, String>(
                    new Tuple<IValue, String>(tp.getFirst().getFirst().clone(),
                            tp.getFirst().getSecond()), tp.getSecond());
            cloned_story.addLast(cloned);
        }
        ch.value = cloned_val;
        ch.story = cloned_story;
        return ch;
    }
}
