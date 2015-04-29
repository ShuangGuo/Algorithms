package others;

public class SimpleMatch {

	private static enum State {
        JUST_STARTED, NORMAL, EAGER, END
    }

    private final int outbound;
    private final int stringOutbound;
    private final String pattern;
    private final String matchString;

    private int position;
    private int stringPos;
    private State state = State.JUST_STARTED;
    private boolean matchFound = false;

    public SimpleMatch(String pattern, String matchString) {
        if (pattern == null || matchString == null || pattern.length() == 0 || matchString.length() == 0) {
            throw new IllegalArgumentException(
                    "Pattern and String must not be null");
        }

        this.pattern = pattern;
        this.matchString = matchString;

        outbound = pattern.length() - 1;
        stringOutbound = matchString.length() - 1;

        position = 0;
        stringPos = 0;
    }

    private void calcState() {
        //calculate state
        if (state == State.END) {
            return;
        }

        if (position > outbound || stringPos > stringOutbound) {
            state = State.END;
        } else if (pattern.charAt(position) == '*') {
            if ((position + 1) > outbound) {
                state = State.END;
                matchFound = true;
            } else {
                state = State.EAGER;
            }
        } else {
            state = State.NORMAL;
        }
    }

    private void eat() {
        //eat a character
        if (state == State.END) {
            return;
        }

        matchFound = false;

        if (state == State.EAGER) {
            position++;
            stringPos++;

            if (match()) {
                stringPos--;
                position--;

                state = State.END;
                matchFound = true;
                return;
            }

            stringPos++;
        } else if (state == State.NORMAL) {
            if (matchOne()) {
            	stringPos++;
                position++;

                matchFound = true;
            } else {
                state = State.END;
            }
        }
    }

    private boolean matchOne() {
        return (pattern.charAt(position) == '?' 
        		|| pattern.charAt(position) == matchString.charAt(stringPos));
    }


    /**
     * Match and return result
     * @return true if match
     */
    public boolean match() {
        if (outbound > stringOutbound) {
            return false;
        }

        while (state != State.END) {
            calcState();
            eat();
        }
        
        return matchFound;
    }

    /**
     * Match and return result
     * @param p pattern
     * @param s string to match
     * @return true if match
     * @throws IllegalArgumentException
     */
    public static boolean match(String p, String s) throws
            IllegalArgumentException {
        return new SimpleMatch(p, s).match();
    }

}