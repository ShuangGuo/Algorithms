package others;

import java.util.ArrayList;

public class Glob {
	private static enum State {
        JUST_STARTED, NORMAL, EAGER, END
    }

    private static int position;
    private static int stringPos;
    private static State state = State.JUST_STARTED;
    private static boolean matchFound = false;
    
	  public static String[] match(String pattern, String[] filenames) {
		  if (pattern == null || pattern.length() == 0 || filenames == null || filenames.length == 0) {
			  return null;
		  }
		  int len = filenames.length;
		  ArrayList<String> list = new ArrayList<String>();
		  for (int i = 0; i < len; i++) {
			  String filename = filenames[i];
			  if (isMatch(pattern, filename)) {
				  list.add(filename);
			  }
		  }
		  System.out.println("lengh of list: " + list.size());
		  for (String s: list) {
			  System.out.println(s);
		  }
		  String[] result = list.toArray(new String[list.size()]);
		  return result;
	  }
	  public static boolean isMatch(String pattern, String matchString) {
          while (state != State.END) {
              calcState(pattern, matchString);
              eat(pattern, matchString);
          }
          return matchFound;
	  }
      private static void calcState(String pattern, String matchString) {
          if (state == State.END) {
              return;
          }

          if (position > pattern.length() - 1 || stringPos > matchString.length() - 1) {
              state = State.END;
          } else if (pattern.charAt(position) == '*') {
              if ((position + 1) > pattern.length() - 1) {
                  state = State.END;
                  matchFound = true;
              } else {
                  state = State.EAGER;
              }
          } else {
              state = State.NORMAL;
          }
      }

      private static void eat(String pattern, String matchString) {
          if (state == State.END) {
              return;
          }
          matchFound = false;
          if (state == State.EAGER) {
              position++;
              stringPos++;

              if (isMatch(pattern, matchString)) {
                  stringPos--;
                  position--;

                  state = State.END;
                  matchFound = true;
                  return;
              }

              stringPos++;
          } else if (state == State.NORMAL) {
              if (matchOne(pattern, matchString)) {
              	stringPos++;
                  position++;
                  matchFound = true;
              } else {
                  state = State.END;
              }
          }
      }

      private static boolean matchOne(String pattern, String matchString) {
          return (pattern.charAt(position) == '?' || pattern.charAt(position) == matchString.charAt(stringPos));
      }
      
	  public static boolean arrayEquals(String[] array1, String[] array2) {
	    if (array1.length != array2.length) {
	      return false;
	    }
	    for (int i = 0;i < array1.length; i++) {
	      if (!array1[i].equals(array2[i])) {
	        return false;
	      }
	    }
	    return true;
	  }

	  public static void main(String[] args) {
	    System.out.println(arrayEquals(new String[] { "abcd", "dabc", "abc" }, 
	          match("?abc*", new String[] { "abcd", "dabc", "abc", "efabc", "eadd" })));
	    System.out.println(arrayEquals(new String[] { "abcd", "dabc", "abc" }, 
	          match("?a**c*", new String[] { "abcd", "dabc", "abc", "efabc", "eadd" })));
	  }
	}
