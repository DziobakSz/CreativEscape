package com.example.finalproject.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.R;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private TextView OpisPL;
    private TextView OpisEN;
    private TextView numbers;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });
        OpisPL = (TextView) root.findViewById(R.id.opisPL);
        OpisEN = (TextView) root.findViewById(R.id.opisEN);
        numbers = (TextView) root.findViewById(R.id.numbers);
        OpisPL.setText("Zdajemy sobie sprawę, że izolacja związana z pandemią może być dla wielu osób trudnym przeżyciem.\n" +
                "Jeżeli czujesz się samotny i odczuwasz potrzebę wsparcia emocjonalnego lub psychicznego, zadzwoń na podany poniżej numer Kryzysowego Telefonu Zaufania lub umów się na wideoporadę psychologiczną ze spejcalistą.\n" +
                "Jeżeli jesteś ofiarą przemocy domowej, zadzwoń na \"Niebieską Linię\", gdzie otrzymasz informację na temat możliwych form otrzymania pomocy.\n" +
                "Jeżeli radzisz sobie ze stratą bliskiej osoby w wyniku pandemii, zadzwoń na Telefon Wsparcia Fundacji Nagle Sami.\n");
        OpisEN.setText("We are aware that social isolation caused be the current state of pandemic can be a hard and upsetting experience.\n" +
                "If you're feeling depressed or uneasy, please contact your local suicide prevention hotline or seek professional help.");
        numbers.setText("Kryzysowy Telefon Zaufania: tel. 116 123\n \n" +
                "\"Niebieska Linia\": tel. 800-12-00-02\n \n" +
                "Telefon Wsparcia Fundacji Nagle Sami: tel. 800 108 108");
        return root;
    }
}