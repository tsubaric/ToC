import java.util.*;

 public class CFGtoCNF {

    private static final int MAX = 20;
    private static String[][] gram = new String[MAX][MAX];
    private static int np;
    private String input;
    private int lineCount;
    private String string;
    private String epselonFound = "";

    private Map<String, List<String>> mapVariableProduction = new LinkedHashMap<>();

    public static void main(String args[]) {

        try (Scanner sc = new Scanner(System.in)) {

            System.out.print("Enter number of lines:");
            int line_count = sc.nextInt();
            String[] str = new String[line_count];
            for (int i = 0; i < line_count; i++) {
                System.out.print("Enter line " + (i + 1) + ": ");
                str[i] = sc.next();
            }

            StringBuilder finalString = new StringBuilder(str[0] + "\n");

            for (int i = 1; i < line_count - 1; i++)
                finalString.append(str[i]).append("\n");
            finalString.append(str[line_count - 1]);
            CFGtoCNF converter = new CFGtoCNF();
            converter.setInputAndLineCount(finalString.toString(), line_count);
            converter.convertCFGtoCNF();
        }
    }

    public void setString(String string) {
        this.string = string;
    }

    public void setInputAndLineCount(String input, int lineCount) {
        this.input = input;
        this.lineCount = lineCount;
    }

    public Map<String, List<String>> getMapVariableProduction() {
        return mapVariableProduction;
    }

    public void convertCFGtoCNF() {
        convertStringtoMap();
        eliminateEpselon();
        removeDuplicateKeyValue();
        eliminateSingleVariable();
        onlyTwoTerminalandOneVariable();
        eliminateThreeTerminal();
    }

    private void eliminateSingleVariable() {
        System.out.println("Remove Single Variable in Every Production ... ");
        for (int i = 0; i < lineCount; i++) {
            removeSingleVariable();
        }
        printMap();
    }

    private void eliminateThreeTerminal() {
        System.out.println("Replace two terminal variable with new variable ... ");
        for (int i = 0; i < lineCount; i++) {
            removeThreeTerminal();
        }
        printMap();
    }

    private void eliminateEpselon() {
        System.out.println("\nRemove Epselon....");
        for (int i = 0; i < lineCount; i++) {
            removeEpselon();
        }
        printMap();
    }

    private String[] splitEnter(String input) {
        String[] tmpArray = new String[lineCount];
        for (int i = 0; i < lineCount; i++) {
            tmpArray = input.split("\\n");
        }
        return tmpArray;
    }

    private void printMap() {
        Iterator<Map.Entry<String, List<String>>> it = mapVariableProduction.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> pair = it.next();
            System.out.println(pair.getKey() + " -> " + pair.getValue());
        }
        System.out.println(" ");
    }

    private void convertStringtoMap() {
        String[] splitedEnterInput = splitEnter(input);

        for (int i = 0; i < splitedEnterInput.length; i++) {
            String[] tempString = splitedEnterInput[i].split("->|\\|");
            String variable = tempString[0].trim();
            String[] production = Arrays.copyOfRange(tempString, 1, tempString.length);
            List<String> productionList = new ArrayList<String>();
            for (int k = 0; k < production.length; k++) {
                production[k] = production[k].trim();
            }
            for (int j = 0; j < production.length; j++) {
                productionList.add(production[j]);
            }
            mapVariableProduction.put(variable, productionList);
        }
    }

    private void removeEpselon() {
        Iterator<Map.Entry<String, List<String>>> it = mapVariableProduction.entrySet().iterator();
        Iterator<Map.Entry<String, List<String>>> it2 = mapVariableProduction.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ArrayList<String> productionRow = (ArrayList<String>) entry.getValue();
            
            if (productionRow.contains("e")) {
                if (productionRow.size() > 1) {
                    productionRow.remove("e");
                    epselonFound = entry.getKey().toString();
                } else {
                    // remove if less than 1
                    epselonFound = entry.getKey().toString();
                    mapVariableProduction.remove(epselonFound);
                }
            }
        }
        while (it2.hasNext()) {
            Map.Entry<String, List<String>> entry = it2.next();
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();

            for (int i = 0; i < productionList.size(); i++) {
                String temp = productionList.get(i);
                for (int j = 0; j < temp.length(); j++) {
                    if (epselonFound.equals(Character.toString(productionList.get(i).charAt(j)))) {
                        if (temp.length() == 2) {
                            temp = temp.replace(epselonFound, "");

                            if (!mapVariableProduction.get(entry.getKey().toString()).contains(temp)) {
                                mapVariableProduction.get(entry.getKey().toString()).add(temp);
                            }
                        } else if (temp.length() == 3) {
                            String deletedTemp = new StringBuilder(temp).deleteCharAt(j).toString();
                            if (!mapVariableProduction.get(entry.getKey().toString()).contains(deletedTemp)) {
                                mapVariableProduction.get(entry.getKey().toString()).add(deletedTemp);
                            }
                        } else if (temp.length() == 4) {
                            String deletedTemp = new StringBuilder(temp).deleteCharAt(j).toString();
                            if (!mapVariableProduction.get(entry.getKey().toString()).contains(deletedTemp)) {
                                mapVariableProduction.get(entry.getKey().toString()).add(deletedTemp);
                            }
                        } else {
                            if (!mapVariableProduction.get(entry.getKey().toString()).contains("e")) {
                                mapVariableProduction.get(entry.getKey().toString()).add("e");
                            }
                        }
                    }
                }
            }
        }
    }

    private void removeDuplicateKeyValue() {
        System.out.println("Remove Duplicate Key Value ... ");
        Iterator<Map.Entry<String, List<String>>> it = mapVariableProduction.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            ArrayList<String> productionRow = (ArrayList<String>) entry.getValue();

            for (int i = 0; i < productionRow.size(); i++) {
                if (productionRow.get(i).contains(entry.getKey().toString())) {
                    productionRow.remove(entry.getKey().toString());
                }
            }
        }
        printMap();
    }

    private void removeSingleVariable() {
        Iterator<Map.Entry<String, List<String>>> it = mapVariableProduction.entrySet().iterator();
        String key = null;

        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = it.next();
            Set<String> set = mapVariableProduction.keySet();
            ArrayList<String> keySet = new ArrayList<String>(set);
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();

            for (int i = 0; i < productionList.size(); i++) {
                String temp = productionList.get(i);

                for (int j = 0; j < temp.length(); j++) {

                    for (int k = 0; k < keySet.size(); k++) {
                        if (keySet.get(k).equals(temp)) {

                            key = entry.getKey().toString();
                            List<String> productionValue = mapVariableProduction.get(temp);
                            productionList.remove(temp);

                            for (int l = 0; l < productionValue.size(); l++) {
                                mapVariableProduction.get(key).add(productionValue.get(l));
                            }
                        }
                    }
                }
            }
        }
    }

    private Boolean checkDuplicateInProductionList(Map<String, List<String>> map, String key) {
        Boolean notFound = true;
        Iterator<Map.Entry<String, List<String>>> itr = map.entrySet().iterator();
        outerloop:
        while (itr.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) itr.next();
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();

            for (int i = 0; i < productionList.size(); i++) {
                if (productionList.size() < 2) {

                    if (productionList.get(i).equals(key)) {
                        notFound = false;
                        break outerloop;
                    } else {
                        notFound = true;
                    }
                }
            }
        }
        return notFound;
    }

    private void onlyTwoTerminalandOneVariable() {
        System.out.println("Assign new variable for two non-terminal or one terminal ... ");
        Iterator<Map.Entry<String, List<String>>> it = mapVariableProduction.entrySet().iterator();
        String key = null;
        int asciiBegin = 71;
        Map<String, List<String>> tempList = new LinkedHashMap<>();

        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) it.next();
            Set<String> set = mapVariableProduction.keySet();
            ArrayList<String> keySet = new ArrayList<String>(set);
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();
            Boolean found1 = false;
            Boolean found2 = false;
            Boolean found = false;

            for (int i = 0; i < productionList.size(); i++) {
                String temp = productionList.get(i);

                for (int j = 0; j < temp.length(); j++) {

                    if (temp.length() == 3) {

                        String newProduction = temp.substring(1, 3);

                        if (checkDuplicateInProductionList(tempList, newProduction) && checkDuplicateInProductionList(mapVariableProduction, newProduction)) {
                            found = true;
                        } else {
                            found = false;
                        }
                        if (found) {

                            ArrayList<String> newVariable = new ArrayList<>();
                            newVariable.add(newProduction);
                            key = Character.toString((char) asciiBegin);

                            tempList.put(key, newVariable);
                            asciiBegin++;
                        }

                    } else if (temp.length() == 2) { // if only two substring

                        for (int k = 0; k < keySet.size(); k++) {

                            if (!keySet.get(k).equals(Character.toString(productionList.get(i).charAt(j)))) { // if substring not equals to keySet
                                found = false;

                            } else {
                                found = true;
                                break;
                            }
                        }

                        if (!found) {
                            String newProduction = Character.toString(productionList.get(i).charAt(j));

                            if (checkDuplicateInProductionList(tempList, newProduction) && checkDuplicateInProductionList(mapVariableProduction, newProduction)) {
                                ArrayList<String> newVariable = new ArrayList<>();
                                newVariable.add(newProduction);
                                key = Character.toString((char) asciiBegin);
                                tempList.put(key, newVariable);
                                asciiBegin++;
                            }
                        }
                    } else if (temp.length() == 4) {
                        String newProduction1 = temp.substring(0, 2); 
                        String newProduction2 = temp.substring(2, 4); 
                        if (checkDuplicateInProductionList(tempList, newProduction1) && checkDuplicateInProductionList(mapVariableProduction, newProduction1)) {
                            found1 = true;
                        } else {
                            found1 = false;
                        }
                        if (checkDuplicateInProductionList(tempList, newProduction2) && checkDuplicateInProductionList(mapVariableProduction, newProduction2)) {
                            found2 = true;
                        } else {
                            found2 = false;
                        }
                        if (found1) {
                            ArrayList<String> newVariable = new ArrayList<>();
                            newVariable.add(newProduction1);
                            key = Character.toString((char) asciiBegin);
                            tempList.put(key, newVariable);
                            asciiBegin++;
                        }
                        if (found2) {
                            ArrayList<String> newVariable = new ArrayList<>();
                            newVariable.add(newProduction2);
                            key = Character.toString((char) asciiBegin);
                            tempList.put(key, newVariable);
                            asciiBegin++;
                        }
                    }
                }
            }
        }
        mapVariableProduction.putAll(tempList);
        printMap();
    }

    private void removeThreeTerminal() {
        Iterator<Map.Entry<String, List<String>>> it = mapVariableProduction.entrySet().iterator();
        ArrayList<String> keyList = new ArrayList<>();
        Iterator<Map.Entry<String, List<String>>> it2 = mapVariableProduction.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) it.next();
            ArrayList<String> productionRow = (ArrayList<String>) entry.getValue();
            if (productionRow.size() < 2) {
                keyList.add(entry.getKey().toString());
            }
        }

        while (it2.hasNext()) {
            Map.Entry<String, List<String>> entry = (Map.Entry<String, List<String>>) it2.next();
            ArrayList<String> productionList = (ArrayList<String>) entry.getValue();
            if (productionList.size() > 1) {
                for (int i = 0; i < productionList.size(); i++) {
                    String temp = productionList.get(i);
                    for (int j = 0; j < temp.length(); j++) {
                        if (temp.length() > 2) {
                            String stringToBeReplaced1 = temp.substring(j, temp.length());
                            String stringToBeReplaced2 = temp.substring(0, temp.length() - j);
                            for (String key : keyList) {
                                List<String> keyValues = new ArrayList<>();
                                keyValues = mapVariableProduction.get(key);
                                String[] values = keyValues.toArray(new String[keyValues.size()]);
                                String value = values[0];
                                if (stringToBeReplaced1.equals(value)) {
                                    mapVariableProduction.get(entry.getKey().toString()).remove(temp);
                                    temp = temp.replace(stringToBeReplaced1, key);
                                    if (!mapVariableProduction.get(entry.getKey().toString()).contains(temp)) {
                                        mapVariableProduction.get(entry.getKey().toString()).add(i, temp);
                                    }
                                } else if (stringToBeReplaced2.equals(value)) {
                                    mapVariableProduction.get(entry.getKey().toString()).remove(temp);
                                    temp = temp.replace(stringToBeReplaced2, key);
                                    if (!mapVariableProduction.get(entry.getKey().toString()).contains(temp)) {
                                        mapVariableProduction.get(entry.getKey().toString()).add(i, temp);
                                    }
                                }
                            }
                        } else if (temp.length() == 2) {
                            for (String key : keyList) {
                                List<String> keyValues = new ArrayList<>();
                                keyValues = mapVariableProduction.get(key);
                                String[] values = keyValues.toArray(new String[keyValues.size()]);
                                String value = values[0];
                                for (int pos = 0; pos < temp.length(); pos++) {
                                    String tempChar = Character.toString(temp.charAt(pos));
                                    if (value.equals(tempChar)) {
                                        mapVariableProduction.get(entry.getKey().toString()).remove(temp);
                                        temp = temp.replace(tempChar, key);
                                        if (!mapVariableProduction.get(entry.getKey().toString()).contains(temp)) {
                                            mapVariableProduction.get(entry.getKey().toString()).add(i, temp);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (productionList.size() == 1) {
                for (int i = 0; i < productionList.size(); i++) {
                    String temp = productionList.get(i);
                    if (temp.length() == 2) {
                        for (String key : keyList) {
                            List<String> keyValues = new ArrayList<>();
                            keyValues = mapVariableProduction.get(key);
                            String[] values = keyValues.toArray(new String[keyValues.size()]);
                            String value = values[0];
                            for (int pos = 0; pos < temp.length(); pos++) {
                                String tempChar = Character.toString(temp.charAt(pos));
                                if (value.equals(tempChar)) {
                                    mapVariableProduction.get(entry.getKey().toString()).remove(temp);
                                    temp = temp.replace(tempChar, key);
                                    if (!mapVariableProduction.get(entry.getKey().toString()).contains(temp)) {
                                        mapVariableProduction.get(entry.getKey().toString()).add(i, temp);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public String concat(String a, String b)
    {
        String r = a;
        r = r.concat(b);
        return r;
    }

    public String search_prod(String p)
    {
        int j, k;
        String r = "";
        for (j = 0; j < np; j++) {
            k = 1;
            while (gram[j][k] != null) {
                if (gram[j][k].equals(p)) {
                    r = concat(r, gram[j][0]);
                }
                k++;
            }
        }
        return r;
    }

    public String gen_comb(String a, String b)
    {
        String pri = a;
        String re = "";
        for (int i = 0; i < a.length(); i++)
            for (int j = 0; j < b.length(); j++) {
                pri = "";
                pri = pri + a.charAt(i) + b.charAt(j);
                re = re + search_prod(pri);
            }
        return re;
    }

    public void Run() {
        String str;
        String r;
        String pr;
        np = mapVariableProduction.size();
        ArrayList<String> keyList = new ArrayList<>();
        for (int i = 0; i < keyList.size(); i++) {
            gram[i][0] = keyList.get(i);
            List<String> productionList = mapVariableProduction.get(keyList.get(i));
            for (int j = 0; j < productionList.size(); j++) {
                gram[i][j + 1] = productionList.get(j);
            }
        }
        String[][] matrix = new String[MAX][MAX];
        String st;
        str = string;
        System.out.println("String obtained: " + str);
        for (int i = 0; i < str.length(); i++)
        {
            r = "";
            st = "";
            st += str.charAt(i);
            for (int j = 0; j < np; j++) {
                int k = 1;
                while (gram[j][k] != null) {
                    if (gram[j][k].equals(st)) {
                        r = concat(r, gram[j][0]);
                    }
                    k++;
                }
            }
            matrix[i][i] = r;
        }
        for (int k = 1; k < str.length(); k++)
        {
            for (int j = k; j < str.length(); j++) {
                r = " ";
                for (int l = j - k; l < j; l++) {
                    pr = gen_comb(matrix[j - k][l], matrix[l + 1][j]);
                    r = concat(r, pr);
                }
                matrix[j - k][j] = r;
            }
        }
        for (int i = 0; i < str.length(); i++)
        {
            int k = 0;
            int l = str.length() - i - 1;
            for (int j = l; j < str.length(); j++) {
                String formattedOutput = String.format("%8s", matrix[k++][j] + " | ");
                System.out.printf(formattedOutput);
            }
            System.out.println(" ");
        }
        return;
    }
}